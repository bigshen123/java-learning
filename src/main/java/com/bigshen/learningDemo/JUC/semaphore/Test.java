package com.bigshen.learningDemo.JUC.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/4 14:10
 */
public class Test {
    public static void main(String[] args) {
        MyPool myPool = new MyPool(20);
        //创建线程池
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        MyThread t1 = new MyThread("任务A", myPool, 3);
        MyThread t2 = new MyThread("任务B", myPool, 12);
        MyThread t3 = new MyThread("任务C", myPool, 7);
        //在线程池中执行任务
        threadPool.execute(t1);
        threadPool.execute(t2);
        threadPool.execute(t3);
        //关闭池
        threadPool.shutdown();
    }
}