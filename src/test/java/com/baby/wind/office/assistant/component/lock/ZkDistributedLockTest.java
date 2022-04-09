package com.baby.wind.office.assistant.component.lock;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class ZkDistributedLockTest {

    int count = 50;

    @Test
    void acquire() throws Exception {
        int size = 50;
        count = size * 2;
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                ZkDistributedLock lock = new ZkDistributedLock();
                try {
                    if (lock.acquire("zcf", 5000, TimeUnit.MILLISECONDS)){
                        count--;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        lock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        Thread.sleep(120000);
        System.out.println(count);
    }

    public static void main(String[] args) throws Exception {
        new ZkDistributedLockTest().acquire();
    }

    @Test
    void testAcquire() {
    }

    @Test
    void release() {
    }
}