package com.baby.wind.office.assistant.component.lock;

import java.util.concurrent.TimeUnit;

public interface DistributedLock {

    /**
     * 不自旋，有结果直接返回
     */
    boolean acquire(String key) throws Exception;

    /**
     * 锁被占用时，自旋等待，直到超时
     */
    boolean acquire(String key, long time, TimeUnit timeUnit) throws Exception;

    /**
     * 释放分布式锁
     */
    void release() throws Exception;
}
