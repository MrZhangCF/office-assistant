package com.baby.wind.office.assistant.factory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

public class ZkConnectInfoFactory {

    private static final ZkConnectInfoFactory INSTANCE = new ZkConnectInfoFactory();

    private static final Log LOG = LogFactory.getLog(ZkConnectInfoFactory.class);
    private static final String CONFIG_FILE_NAME = "/config/zkConfig.properties";
    private static final String CONFIG_NAME = "zk.host.info";
    private static final String CONNECTOR = ",";
    private static final int RANDOM_BOUND = 1000;

    private ZkConnectInfoFactory(){}

    public static ZkConnectInfoFactory getInstance(){
        return INSTANCE;
    }

    private static String connectStr;

    public String connectString(){
        String[] split = connectStr.split(CONNECTOR);
        return split[new Random().nextInt(RANDOM_BOUND) % split.length];
    }

    private static void initConnectStr() {
        try {
            InputStream in = ZkConnectInfoFactory.class.getResourceAsStream(CONFIG_FILE_NAME);
            if (Objects.isNull(in)){
                LOG.info("initialize zk connect str error : file is not exist in [" + CONFIG_FILE_NAME + "]");
                return;
            }
            Properties properties = new Properties();
            properties.load(in);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                if (key.equals(CONFIG_NAME)) {
                    connectStr = value;
                    break;
                }
            }
        }catch (Exception e){
            LOG.info("initialize connect str error", e);
        }
    }

    static {
        initConnectStr();
    }
}
