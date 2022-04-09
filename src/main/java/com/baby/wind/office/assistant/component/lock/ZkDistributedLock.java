package com.baby.wind.office.assistant.component.lock;

import com.baby.wind.office.assistant.factory.RedisClientFactory;
import com.baby.wind.office.assistant.factory.ZkClientFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.*;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ZkDistributedLock implements DistributedLock{

    private static final Log LOG = LogFactory.getLog(ZkDistributedLock.class);
    private static final String BASE = "/locker";
    private final ThreadLocal<String> curPathCache = new ThreadLocal<>();

    @Override
    public boolean acquire(String key) throws Exception {
        return acquire(key, 0, null);
    }

    @Override
    public boolean acquire(String key, long time, TimeUnit timeUnit) throws Exception {
        ZooKeeper client = ZkClientFactory.getInstance().client();
        checkRootExist(client);

        String curPath = client.create(buildPath(key), "".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL);

        curPathCache.set(curPath);

        String nodeName = parseNodeName(curPath);

        List<String> children = client.getChildren(BASE, false).stream()
                .filter(item -> validPath(item, key))
                .sorted(Comparator.comparing(o -> parseSequence(o, key)))
                .collect(Collectors.toList());

        int index = children.indexOf(nodeName);
        if (index < 0){
            LOG.error("cannot find current path");
            release();
            throw new Exception("cannot find current path");
        }
        if (index == 0){
            return true;
        }
        if (time == 0){
            release();
            return false;
        }
        CountDownLatch latch = new CountDownLatch(1);
        client.exists(BASE.concat("/").concat(children.get(index - 1)), watchedEvent -> {
            if (Watcher.Event.EventType.NodeDeleted == watchedEvent.getType()) {
                latch.countDown();
            }
        });
        boolean await = latch.await(time, timeUnit);
        if (await){
            return true;
        }else {
            RedisClientFactory.client().sadd("lock", Thread.currentThread().getName());
            release();
            return false;
        }
    }

    @Override
    public void release() throws Exception {
        try {
            String curPath = curPathCache.get();
            if (curPath == null || curPath.trim().length() == 0) {
                return;
            }
            ZkClientFactory.getInstance().client().delete(curPath, -1);
        }finally {
            curPathCache.remove();
        }
    }

    private String buildPath(String key){
        return BASE.concat("/").concat(nodeNamePrefix(key));
    }

    private String nodeNamePrefix(String key){
        return key.concat("-");
    }

    private String parseNodeName(String path){
        return path.substring(BASE.length() + 1);
    }

    private String parseSequence(String path, String key){
        int index = path.lastIndexOf(nodeNamePrefix(key));
        return path.substring(index + key.length());
    }

    /**
     * 判断结点是否合法
     */
    private boolean validPath(String path, String curKey){
        String pathPrefix = nodeNamePrefix(curKey);
        if (path.length() <= pathPrefix.length() || !path.startsWith(pathPrefix)){
            return false;
        }
        String suffix = path.substring(pathPrefix.length());
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(suffix).matches();
    }

    private void checkRootExist(ZooKeeper client) throws KeeperException, InterruptedException {
        if (client.exists(BASE, false) == null){
            client.create(BASE, "".getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        }
    }
}
