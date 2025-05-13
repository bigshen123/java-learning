package com.bigshen.learningDemo.JUC.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author byj
 * @date 2025/3/25
 * @Description condition demo
 *
 * 独占锁和读写锁的写锁都支持Condition，但是读写锁的读锁是不支持Condition的。

 * 线程1 -> 获取锁 -> 释放锁 + await()阻塞等待 ->

 * 线程2 -> 获取锁 -> signal()唤醒线程1 + 释放锁 ->

 * 线程1 -> 被唤醒 + 尝试获取锁 -> 释放锁
 */
public class ConditionDemo {
    static ReentrantLock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            lock.lock();
            System.out.println("第一个线程加锁");
            try {
                System.out.println("第一个线程释放锁以及阻塞等待");
                condition.await();
                System.out.println("第一个线程重新获取锁");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("第一个线程释放锁");
            lock.unlock();
        }).start();
        Thread.sleep(3000);

        new Thread(() -> {
            lock.lock();
            System.out.println("第二个线程加锁");
            System.out.println("第二个线程唤醒第一个线程");
            condition.signal();
            lock.unlock();
            System.out.println("第二个线程释放锁");
        }).start();
    }
}
