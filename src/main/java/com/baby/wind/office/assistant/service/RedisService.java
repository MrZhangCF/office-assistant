package com.baby.wind.office.assistant.service;

import redis.clients.jedis.Jedis;

public class RedisService {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("124.221.221.235",6379);
        System.out.println(jedis.smembers("lock"));
        jedis.del("lock");
        //[Thread-44, Thread-22, Thread-43, Thread-20, Thread-42, Thread-41, Thread-26, Thread-48, Thread-25, Thread-47, Thread-46, Thread-23, Thread-45, Thread-29, Thread-28, Thread-49, Thread-27, Thread-3, Thread-4, Thread-5, Thread-6, Thread-8, Thread-9, Thread-40, Thread-11, Thread-10, Thread-31, Thread-37, Thread-14, Thread-13, Thread-34, Thread-18, Thread-17, Thread-39, Thread-38, Thread-0, Thread-1]
    }
}
