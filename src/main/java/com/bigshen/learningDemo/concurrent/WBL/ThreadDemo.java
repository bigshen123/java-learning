package com.bigshen.learningDemo.concurrent.WBL;

import java.util.concurrent.*;

/**
 * @ClassName Test
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/18
 * @Version V1.0
 **/
public class ThreadDemo {
    @org.junit.Test
    public void test01() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t2.start();        //实现等待
        t1.join();
        t2.join();
        System.out.println("=============");
    }
    @org.junit.Test
    public void test02() throws InterruptedException {
        Executor executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);
        executor.execute(()->{
            try {
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName());
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        executor.execute(()->{
            try {
                Thread.sleep(2000);
                System.out.println(Thread.currentThread().getName());
                latch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        latch.await();
        System.out.println("=============");
    }
}
