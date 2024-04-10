package com.bigshen.learningDemo.JUC.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName RedisLock
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/8/27
 * @Version V1.0
 **/


public class RedisLock {

    private static final Logger logger = LoggerFactory.getLogger(RedisLock.class);

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME_SECOND = "EX";

    private static final int ACQUIRE_LOCK_TIME_OUT_IN_MS = 5*1000;//获取锁超时时间
    private static final int EXPIRE_IN_SECOND = 5;                  //锁超时时间
    private static final int WAIT_INTERVAL_IN_MS = 100;             //自旋重试间隔

    private static JedisPool jedisPool = new JedisPool();


    /**
     * 使用 set key value expireTime 获取锁
     * @param lockKey
     * @return
     */
    public static boolean tryLockWithSet(String lockKey){
        boolean flag = false;
        long timeoutAt = System.currentTimeMillis() + ACQUIRE_LOCK_TIME_OUT_IN_MS;    //此次获取锁的超时时间点
        try (Jedis jedis = jedisPool.getResource()){
            String result;
            while (true) {
                long now = System.currentTimeMillis();
                if(timeoutAt < now){
                    break;
                }
                result = jedis.set(lockKey, SET_IF_NOT_EXIST);
                if(LOCK_SUCCESS.equals(result)){
                    flag = true;
                    return flag;
                }
                TimeUnit.NANOSECONDS.sleep(WAIT_INTERVAL_IN_MS);
            }

        } catch (InterruptedException e) {
            logger.error("accquire redis lock error...", e);
            e.printStackTrace();
        }

        if(!flag){
            logger.error("cannot accquire redis lock...");
        }

        return flag;
    }

    /**
     * 使用 setnx 和 getset 方式获取锁
     * @param lockKey
     * @return
     */
    public static boolean tryLockWithSetnx(String lockKey){
        boolean flag = false;
        try (Jedis jedis = jedisPool.getResource()) {
            long timeoutAt = System.currentTimeMillis() + ACQUIRE_LOCK_TIME_OUT_IN_MS;    //此次获取锁的超时时间点
            while (true){
                long now = System.currentTimeMillis();
                if(timeoutAt < now){
                    break;
                }

                String expireAt = String.valueOf(now + EXPIRE_IN_SECOND*1000);  //过期时间戳作为value
                long ret = jedis.setnx(lockKey, expireAt);
                if(ret == 1){//已取得锁
                    flag = true;
                    return flag;
                }else {
                    // 未获取锁，尝试重新获取
                    // 此处使用double check 的思想，防止多线程同时竞争到锁
                    // 1) 先获取上一个锁的过期时间，校验当前是否过期。
                    // 2) 如果过期了，尝试使用getset方式获取锁。此处可能存在多个线程同时执行到的情况。
                    // 3) getset更新过期时间，并且获取上一个锁的过期时间。
                    // 4) 如果getset获取到的oldExpireAt 已过期，说明获取锁成功。
                    //    如果和当前比未过期，说明已经有另一个线程提前获取到了锁
                    //    这样也没问题，只是短暂的将上一个锁稍微延后一点时间（只有在A和B线程同时执行到getset时，才会出现，延长的时间很短）
                    String oldExpireAt = jedis.get(lockKey);
                    if(oldExpireAt != null && Long.valueOf(oldExpireAt) < now){
                        oldExpireAt = jedis.getSet(lockKey, expireAt);
                        if(Long.parseLong(oldExpireAt) < now){
                            flag = true;
                            return flag;
                        }
                    }
                }

                TimeUnit.NANOSECONDS.sleep(WAIT_INTERVAL_IN_MS);
            }
        } catch (InterruptedException e) {
            logger.error("accquire redis lock error...", e);
            e.printStackTrace();
        }

        if(!flag){
            logger.error("cannot accquire redis lock...");
        }

        return flag;
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unLock(String lockKey){
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.del(lockKey);
        }
    }

}
