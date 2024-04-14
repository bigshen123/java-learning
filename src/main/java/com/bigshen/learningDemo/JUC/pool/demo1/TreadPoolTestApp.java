package com.bigshen.learningDemo.JUC.pool.demo1;

import cn.hutool.core.thread.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author BYJ
 * @Date 2024/4/10 21:40
 * @Describe
 */
public class TreadPoolTestApp {
    public static void main(String[] args) {
        // 创建自定义的拒绝策略
        CustomRejectedExecutionHandler rejectionHandler = new CustomRejectedExecutionHandler();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                5,                                        //核心线程数
                10,                                       //最大线程数
                200,                                      //非核心线程数存活时间数
                TimeUnit.MILLISECONDS,                    //非核心线程数存活时间单位
                new LinkedBlockingQueue<>(5),             //阻塞队列
                new ThreadFactoryBuilder().build(),       //线程工厂
                // 使用自定义的拒绝策略 new ThreadPoolExecutor.AbortPolicy()
                rejectionHandler);

        for (int i = 0; i < 20; i++) {
            try {
                MyTask myTask = new MyTask(i);
                executor.execute(myTask);
                System.out.println("线程池中线程数目：" + executor.getPoolSize() +
                        "，队列中等待执行的任务数目：" + executor.getQueue().size() +
                        "，已执行完成的任务数目：" + executor.getCompletedTaskCount());

                //此处等待10ms主要是为了让异常栈打印的更整齐，没有其他任何意义
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        System.out.println("-----------------over---------------------");
        System.out.println("线程池中线程数目：" + executor.getPoolSize() +
                "，队列中等待执行的任务数目：" + executor.getQueue().size() +
                "，已执行完成的任务数目：" + executor.getCompletedTaskCount());
    }

    static class MyTask implements Runnable {
        private final int taskNum;

        public MyTask(int num) {
            this.taskNum = num;
        }

        @Override
        public void run() {
            System.out.println("正在执行task-" + taskNum);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("task-" + taskNum + "执行完毕");
        }
    }
}
