package com.bigshen.learningDemo.JUC.lock.redisLock;

/**
 * @ClassName DistributedLock
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/11
 */

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.UUID;

/**
 * 2  * �ֲ�ʽ���ļ�ʵ�ִ���
 */
public class DistributedLock {

    private final JedisPool jedisPool;

    public DistributedLock(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    /**
     * 14      * ����
     * 15      * @param lockName       ����key
     * 16      * @param acquireTimeout ��ȡ��ʱʱ��
     * 17      * @param timeout        ���ĳ�ʱʱ��
     * 18      * @return ����ʶ
     * 19
     */
    public String lockWithTimeout(String lockName, long acquireTimeout, long timeout) {
        Jedis conn = null;
        String retIdentifier = null;
        try {
            // ��ȡ����
            conn = jedisPool.getResource();
            // �������һ��value
            String identifier = UUID.randomUUID().toString();
            // ��������keyֵ
            String lockKey = "lock:" + lockName;
            // ��ʱʱ�䣬�����󳬹���ʱ�����Զ��ͷ���
            int lockExpire = (int) (timeout / 1000);

            // ��ȡ���ĳ�ʱʱ�䣬�������ʱ���������ȡ��
            long end = System.currentTimeMillis() + acquireTimeout;
            while (System.currentTimeMillis() < end) {
                if (conn.setnx(lockKey, identifier) == 1) {
                    conn.expire(lockKey, lockExpire);
                    // ����valueֵ�������ͷ���ʱ��ȷ��
                    retIdentifier = identifier;
                    return retIdentifier;
                }
                // ����-1����keyû�����ó�ʱʱ�䣬Ϊkey����һ����ʱʱ��
                if (conn.ttl(lockKey) == -1) {
                    conn.expire(lockKey, lockExpire);
                }

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return retIdentifier;
    }

    /**
     * 64      * �ͷ���
     * 65      * @param lockName   ����key
     * 66      * @param identifier �ͷ����ı�ʶ
     * 67      * @return
     * 68
     */
    public boolean releaseLock(String lockName, String identifier) {
        Jedis conn = null;
        String lockKey = "lock:" + lockName;
        boolean retFlag = false;
        try {
            conn = jedisPool.getResource();
            while (true) {
                // ����lock��׼����ʼ����
                conn.watch(lockKey);
                // ͨ��ǰ�淵�ص�valueֵ�ж��ǲ��Ǹ��������Ǹ�������ɾ�����ͷ���
                if (identifier.equals(conn.get(lockKey))) {
                    Transaction transaction = conn.multi();
                    transaction.del(lockKey);
                    List<Object> results = transaction.exec();
                    if (results == null) {
                        continue;
                    }
                    retFlag = true;
                }
                conn.unwatch();
                break;
            }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return retFlag;
    }
}

