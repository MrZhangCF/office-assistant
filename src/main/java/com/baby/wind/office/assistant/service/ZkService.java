package com.baby.wind.office.assistant.service;

import com.baby.wind.office.assistant.factory.ZkClientFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

@Component
public class ZkService {

    private static final Log LOG = LogFactory.getLog(ZkService.class);

    public void create(String path, String data) {
        try {
            ZkClientFactory.getInstance().client().create(path, data.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL);
        }catch (Exception e){
            LOG.error("ZkHandler#create exception", e);
        }
    }

    public String getData(String path) {
        try {
            return new String(ZkClientFactory.getInstance().client().getData(path, false, new Stat()));
        }catch (Exception e){
            LOG.error("ZkHandler#getData exception", e);
        }
        return null;
    }

}
