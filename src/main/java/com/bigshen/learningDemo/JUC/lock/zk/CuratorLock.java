package com.bigshen.learningDemo.JUC.lock.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

/**
 * @ClassName: CuratorLock
 * @Description: curator分布式锁
 * 实现步骤
 *     1.所有客户端在/sharelock创建自己的锁节点(顺序临时节点)
 *     2.获取/sharelock下所有节点
 *     3.读锁客户端判断自己前面有没有写锁，没有则获得锁，有则监听写锁；写锁客户端判断自己前面有没有节点，无节点则获得锁，有则监听前一个节点
 *     4.持有锁的客户端删除节点，某个客户端获得通知，得到锁
 *     5.重复步骤4
 */
public class CuratorLock {

    private final InterProcessMutex processLock;

    public CuratorLock() {
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181",
                (retry, sleepTime, retrySleeper) -> true);
        // client操作需要先start
        client.start();
        // zk锁
        processLock = new InterProcessMutex(client, "/lock");
    }

    public boolean lock() {
        try {
            processLock.acquire();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean unlock() {
        try {
            processLock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}