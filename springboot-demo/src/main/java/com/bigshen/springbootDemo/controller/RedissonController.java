package com.bigshen.springbootDemo.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2025/5/20
 * @Description
 */
@RestController
public class RedissonController {

    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/redisson/{key}")
    public String redissonTest(@PathVariable("key") String lockKey) {
        // 把 lock 和 bucket 分开，使用不同的 key
        String bucketKey = lockKey + ":bucket";
        String lockKeyReal = lockKey + ":lock";

        RBucket<Object> bucket = redissonClient.getBucket(bucketKey);
        bucket.set("123");
        bucket.expire(100, TimeUnit.SECONDS);

        RLock lock = redissonClient.getLock(lockKeyReal);
        try {
            lock.lock();
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return "已解锁";
    }
}
