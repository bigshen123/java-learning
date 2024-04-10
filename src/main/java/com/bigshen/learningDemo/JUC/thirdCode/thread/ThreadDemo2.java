package com.bigshen.learningDemo.JUC.thirdCode.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:创建 10个线程 线程 1打印1-10 线程2 打印11-20 线程3 打印21-30 ...线程10打印91-100
 * @Author: BIGSHEN
 * @Date: 2019/12/15 17:36
 */
public class ThreadDemo2 {
    private static AtomicInteger count = new AtomicInteger(1);

    public static void main(String[] args) {
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() <= 10) {
                    System.out.println("线程1:"+ count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 10 && count.get()<=20) {
                    System.out.println("线程2:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 20 && count.get()<=30) {
                    System.out.println("线程3:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 30 && count.get()<=40) {
                    System.out.println("线程4:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 40 && count.get()<=50) {
                    System.out.println("线程5:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 50 && count.get()<=60) {
                    System.out.println("线程6:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 60 && count.get()<=70) {
                    System.out.println("线程7:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 70 && count.get()<=80) {
                    System.out.println("线程8:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 80 && count.get()<=90) {
                    System.out.println("线程9:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();
        new Thread(() -> {
            while (count.get() <= 100) {
                if (count.get() > 90 && count.get()<=100) {
                    System.out.println("线程10:"+count.get());
                    count.incrementAndGet();
                }
            }
        }).start();

    }
}
