package com.bigshen.learningDemo.leetcode.lock;

import redis.clients.jedis.Jedis;
/**
 * @author byj
 * @date 2024/7/4
 * @Description 使用redis实现一个可重入锁
 */
public class RedisReentrantLock {
    private Jedis jedis;
    private String lockKey;
    private String threadId;
    private int lockCount;

    public RedisReentrantLock(Jedis jedis, String lockKey) {
        this.jedis = jedis;
        this.lockKey = lockKey;
        this.threadId = Thread.currentThread().getId() + "-" + Thread.currentThread().getName();
        this.lockCount = 0;
    }

    public synchronized boolean acquire(int timeout) {
        long expireTime = System.currentTimeMillis() + timeout * 1000L;
        String expireTimeStr = String.valueOf(expireTime);

        // Lua script for atomic acquire
        String acquireScript = "if redis.call('exists', KEYS[1]) == 0 or redis.call('hget', KEYS[1], 'owner') == ARGV[1] then " +
                "redis.call('hset', KEYS[1], 'owner', ARGV[1]) " +
                "redis.call('hincrby', KEYS[1], 'count', 1) " +
                "redis.call('pexpire', KEYS[1], ARGV[2]) " +
                "return 1 " +
                "else " +
                "return 0 " +
                "end";

        Object result = jedis.eval(acquireScript, 1, lockKey, threadId, expireTimeStr);

        if ("1".equals(result.toString())) {
            lockCount++;
            return true;
        }
        return false;
    }

    public synchronized boolean release() {
        // Lua script for atomic release
        String releaseScript = "if redis.call('hexists', KEYS[1], 'owner') == 1 and redis.call('hget', KEYS[1], 'owner') == ARGV[1] then " +
                "local count = redis.call('hincrby', KEYS[1], 'count', -1) " +
                "if count == 0 then " +
                "redis.call('del', KEYS[1]) " +
                "else " +
                "redis.call('pexpire', KEYS[1], ARGV[2]) " +
                "end " +
                "return 1 " +
                "else " +
                "return 0 " +
                "end";

        long expireTime = System.currentTimeMillis() + 1000 * 10; // Extend the lock expiration time for reentrant
        String expireTimeStr = String.valueOf(expireTime);

        Object result = jedis.eval(releaseScript, 1, lockKey, threadId, expireTimeStr);

        if ("1".equals(result.toString())) {
            lockCount--;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost", 6379);
        RedisReentrantLock lock = new RedisReentrantLock(jedis, "my_lock");

        // Acquiring the lock
        if (lock.acquire(10)) {
            System.out.println("Lock acquired!");
            // Perform your critical section operations here

            // Releasing the lock
            if (lock.release()) {
                System.out.println("Lock released!");
            } else {
                System.out.println("Failed to release lock!");
            }
        } else {
            System.out.println("Failed to acquire lock!");
        }

        jedis.close();
    }
}
