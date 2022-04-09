package com.baby.wind.office.assistant.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZkClientFactory {

    private static final Log LOG = LogFactory.getLog(ZkClientFactory.class);

    private static final int DEFAULT_TIME_OUT = 5000;

    private static final ZkClientFactory INSTANCE = new ZkClientFactory();

    private ZkClientFactory(){}

    public static ZkClientFactory getInstance(){
        return INSTANCE;
    }

    public ZooKeeper client() throws Exception{
        long cur = System.currentTimeMillis();
        String connectString = ZkConnectInfoFactory.getInstance().connectString();
        CountDownLatch latch = new CountDownLatch(1);
        Watcher watcher = event -> {
            if (Watcher.Event.KeeperState.SyncConnected == event.getState()) {
                latch.countDown();
            }
        };
        ZooKeeper zooKeeper = new ZooKeeper(connectString, DEFAULT_TIME_OUT, watcher);
        long spend = System.currentTimeMillis() - cur;
        boolean success = latch.await(DEFAULT_TIME_OUT - spend, TimeUnit.MILLISECONDS);
        if (success){
            return zooKeeper;
        }else {
            LOG.error("connect zk server time out");
            throw new Exception("connect zk server time out");
        }
    }
}
