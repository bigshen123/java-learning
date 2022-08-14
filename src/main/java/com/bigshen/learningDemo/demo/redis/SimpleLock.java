package com.bigshen.learningDemo.demo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.UUID;

/**
 * @ClassName SimpleLock
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/9/6
 * @Version V1.0
 **/
public class SimpleLock {
    private final String LOCK_NAME="lock";
    Jedis conn=new Jedis("127.0.0.1",6379);

    /**
     * ?????????
     * @return
     */
    private String accquireLock(int timeout){
        String uuid= UUID.randomUUID().toString();
        long end=System.currentTimeMillis();
        //?????????????
        while (System.currentTimeMillis()>end){
            if (conn.setnx(LOCK_NAME,uuid).intValue()==1){
                conn.expire(LOCK_NAME,timeout);
                return uuid;
            }
            if (conn.ttl(LOCK_NAME)==-1){
                conn.expire(LOCK_NAME,timeout);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * ????????
     * @param uuid
     * @return
     */
    private Boolean releaseLock(String uuid){
        while (true){
            conn.watch(LOCK_NAME);
            if (uuid.equals(conn.get(LOCK_NAME))){
                Transaction transaction = conn.multi();
                transaction.del(LOCK_NAME);
                if (transaction.exec()==null){
                    continue;
                }
                return true;
            }
            conn.unwatch();
            break;
        }
        return false;
    }

    public static void main(String[] args) {
        SimpleLock simpleLock=new SimpleLock();
        String uuid = simpleLock.accquireLock(100000);
        if (uuid!=null){
            System.out.println("????????");
        }else{
            System.out.println("????????......");
            }

    }
}
