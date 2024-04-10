package com.bigshen.learningDemo.JUC.thirdCode.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: 创建 10个线程 线程 1打印1-10 线程2 打印11-20 线程3 打印21-30 ...线程10打印91-100
 * @Author: BIGSHEN
 * @Date: 2019/12/15 16:55
 */
public class ThreadDemo {
    public static AtomicInteger i = new AtomicInteger(1);

    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(10);

        exec.execute(() -> {
            for (int j = 0; j < 10; j++) {
                System.out.println("线程" + i);
                i.incrementAndGet();
            }
        });
        exec.shutdown();
    }
}
