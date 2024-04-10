package com.bigshen.learningDemo.JUC.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 3个线程卖票，每个线程卖一张票后休息100毫秒
 * @Author: BIGSHEN
 * @Date: 2019/12/17 16:57
 */
public class LockTest {
    public static void main(String[] args) {
       final Ticket t = new Ticket();
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    t.sale();
                }
            }, "" + i).start();
        }
    }

    static class Ticket {
        int ticket = 30;
        private Lock lock = new ReentrantLock();
        private Condition condition = lock.newCondition();
        int count = 0;

        private void sale() {
            try {
                lock.lock();
                int name = Integer.parseInt(Thread.currentThread().getName());
                while ((count % 3) != name) {
                    try {
                        condition.await();
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (ticket > 0) {
                    System.out.print("售票点");
                    System.out.println(name + "售票完成。还剩" + --ticket + "张票");
                    break;
                }
                count = (count % 3) + 1;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }
}
