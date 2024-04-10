package com.bigshen.learningDemo.JUC.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Author BYJ
 * @Date 2020/12/17 22:14
 * @Describe 初始化一个 CountDownLatch 时告诉并发的线程，然后在每个线程处理完毕之后调用 countDown() 方法。
 *          该方法会将 AQS 内置的一个 state 状态 -1 。
 *          最终在主线程调用 await() 方法，它会阻塞直到 state == 0 的时候返回。
 */
public class CountDownLatchDemo {

    private static void countDownLatch() throws Exception{
        int thread = 3 ;
        long start = System.currentTimeMillis();
        final CountDownLatch countDown = new CountDownLatch(thread);
        for (int i= 0 ;i<thread ; i++){
            new Thread(() -> {
                System.out.println("thread run");
                try {
                    Thread.sleep(2000);
                    countDown.countDown();
                    System.out.println("thread end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }

        countDown.await();
        long stop = System.currentTimeMillis();
        System.out.println("main over total time="+ (stop - start));;
    }

    public static void main(String[] args) throws Exception {
        countDownLatch();
    }
}
