package com.bigshen.learningDemo.JUC.lock.redisson;

import org.junit.Test;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedissonTest
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/17
 */
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    /**
     *
     */
    @Test
    public void redissonDemo1() throws InterruptedException {
        RLock lock = redissonClient.getLock("anyLock");
        //最简单的一种方法 （默认）  非公平锁
        lock.lock();
        lock.lock(10, TimeUnit.SECONDS);
        boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);
        if (res) {
            try {

            } finally {
                lock.unlock();
            }
        }
        //异步锁
        lock.lockAsync();
        lock.lockAsync(10,TimeUnit.SECONDS);
        Future<Boolean> asyncRes = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);

        //公平锁
        RLock fairLock = redissonClient.getFairLock("anyLock");
        fairLock.lock();
        fairLock.lock(10,TimeUnit.SECONDS);
        boolean fairRes = fairLock.tryLock(100, 10, TimeUnit.SECONDS);
        fairLock.unlock();

        //复用锁
        RLock lock1 = redissonClient.getLock("lock1");
        RLock lock2 = redissonClient.getLock("lock2");
        RLock lock3 = redissonClient.getLock("lock3");
        RedissonMultiLock multiLock= (RedissonMultiLock) redissonClient.getMultiLock(lock1,lock2,lock3);
        multiLock.lock();
        multiLock.unlock();

        //读锁
        redissonClient.getReadWriteLock("");
    }
}
