package com.bigshen.learningDemo.JUC.semaphore;

import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * @author byj
 * @date 2024/1/19
 * @Description
 */
public class SemaphoreTest {
    @Test
    public void fair() throws Exception {
        // 创建Semaphore对象，且指定公平锁的工作方式
        Semaphore semaphore = new Semaphore(1, true);

        // 创建一个简单任务
        Runnable runnable = () -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " 执行了");
            } catch (InterruptedException e) {
                // ignore
            } finally {
                semaphore.release();
            }
        };

        // 创建多个线程来执行同一个任务
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(runnable, "线程" + i);
            thread.start();
            // 放弃时间片，确保上一线程已经拿到许可证或者已经进入阻塞状态
            for (int k = 0; k < 100; k++) {
                Thread.yield();
            }
        }

        // 主线程睡眠1秒，以便观察现象
        Thread.sleep(1000);
    }

    @Test
    public void unFair() throws Exception {
        // 创建Semaphore对象，且指定非公平锁的工作方式
        Semaphore semaphore = new Semaphore(1, false);

        // 创建一个简单任务
        Runnable runnable = () -> {
            try {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + " 执行了");
            } catch (InterruptedException e) {
                // ignore
            } finally {
                semaphore.release();
            }
        };

        // 创建多个线程来执行同一个任务
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(runnable, "线程" + i);
            thread.start();
            // 放弃时间片，确保上一线程已经拿到许可证或者已经进入阻塞状态
            for (int k = 0; k < 100; k++) {
                Thread.yield();
            }
        }

        // 主线程睡眠1秒，以便观察现象
        Thread.sleep(1000);
    }

}
