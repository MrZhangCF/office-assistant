package com.baby.wind.office.assistant.factory;

import redis.clients.jedis.Jedis;

public class RedisClientFactory {

    public static Jedis client(){
        return new Jedis("124.221.221.235",6379);
    }
}
