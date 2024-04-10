package com.bigshen.learningDemo.JUC.cyclicbarrier;

import java.util.concurrent.*;

/**
 * @Description:测试 CyclicBarrier 类中带参数的 await() 方法
 * @Author: BIGSHEN
 * @Date: 2019/12/26 18:07
 */
public class CyclicBarrierExample2 {
    private static final int threadCount=500;
    private static final CyclicBarrier cyclicBarrier=new CyclicBarrier(5);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < threadCount; i++) {
            final int threadNum=i;
            Thread.sleep(1000);
            threadPool.execute(()->{
                try {
                    test(threadNum);
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        threadPool.shutdown();
    }
    private static void test(int threadnum) throws InterruptedException, BrokenBarrierException {
        System.out.println("threadnum:" + threadnum + "is ready");
        try {
            /*等待60秒，保证子线程完全执行结束*/
            cyclicBarrier.await(60, TimeUnit.SECONDS);
        } catch (Exception e) {
            System.out.println("-----CyclicBarrierException------");
        }
        System.out.println("threadnum:" + threadnum + "is finish");
    }
}
