package com.bigshen.learningDemo.JUC.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @Author BYJ
 * @Date 2024/10/29 21:27
 * @Describe
 */
public class SemaphoreDemo {

    public final static int SEM_SIZE = 10;

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(SEM_SIZE);
        MyThread2 t1 = new MyThread2("t1", semaphore);
        MyThread2 t2 = new MyThread2("t2", semaphore);
        t1.start();
        t2.start();
        int permits = 5;
        System.out.println(Thread.currentThread().getName() + " trying to acquire");
        try {
            semaphore.acquire(permits);
            System.out.println(Thread.currentThread().getName() + " acquire successfully");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
            System.out.println(Thread.currentThread().getName() + " release successfully");
        }
    }
    static class MyThread2 extends Thread {
        private Semaphore semaphore;

        public MyThread2(String name, Semaphore semaphore) {
            super(name);
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            int count = 3;
            System.out.println(Thread.currentThread().getName() + " trying to acquire");
            try {
                semaphore.acquire(count);
                System.out.println(Thread.currentThread().getName() + " acquire successfully");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(count);
                System.out.println(Thread.currentThread().getName() + " release successfully");
            }
        }
    }
}
