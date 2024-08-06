package com.bigshen.learningDemo.leetcode.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author byj
 * @date 2024/7/4
 * @Description
 */
public class RedisReentrantLock2 {

    private final JedisPool jedisPool;
    private final ThreadLocal<String> threadLocalId;
    private final String lockKey;
    private final int expireTime;

    public RedisReentrantLock2(String lockKey, int expireTime) {
        this.jedisPool = new JedisPool(new JedisPoolConfig(), "localhost", 6379);
        this.threadLocalId = ThreadLocal.withInitial(() -> Thread.currentThread().getName() + "-" + System.currentTimeMillis());
        this.lockKey = lockKey;
        this.expireTime = expireTime;
    }

    public boolean acquire() {
        String threadId = threadLocalId.get();
        try (Jedis jedis = jedisPool.getResource()) {
            //String result = jedis.set(lockKey, threadId, "NX", "PX", expireTime);
            String result = jedis.set(lockKey, threadId);
            if ("OK".equals(result)) {
                return true;
            } else if (threadId.equals(jedis.get(lockKey))) {
                jedis.pexpire(lockKey, expireTime);
                return true;
            }
        }finally {
            threadLocalId.remove();
        }
        return false;
    }

    public void release() {
        String threadId = threadLocalId.get();
        try (Jedis jedis = jedisPool.getResource()) {
            if (threadId.equals(jedis.get(lockKey))) {
                jedis.del(lockKey);
            }
        }finally {
            threadLocalId.remove();
        }
    }

    public static void main(String[] args) {
        RedisReentrantLock2 lock = new RedisReentrantLock2("myLock", 10000);

        Runnable task = () -> {
            if (lock.acquire()) {
                try {
                    System.out.println(Thread.currentThread().getName() + " acquired the lock.");
                    // Perform some work with the lock held
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.release();
                    System.out.println(Thread.currentThread().getName() + " released the lock.");
                }
            } else {
                System.out.println(Thread.currentThread().getName() + " could not acquire the lock.");
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();
    }
}
