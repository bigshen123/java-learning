package com.bigshen.learningDemo.JUC.thirdCode.thread;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/1 15:59
 */
public class ExchangerTest {
    private static final Exchanger<String> exchanger = new Exchanger<>();

    private static ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        threadPool.execute(() -> {
            String A = "I'm A!";
            try {
                String B = exchanger.exchange(A);
                System.out.println("In 1-" + Thread.currentThread().getName() + ": " + B);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadPool.execute(() -> {
            try {
                String B = "I'm B!";
                String A = exchanger.exchange(B);
                System.out.println("In 2-" + Thread.currentThread().getName() + ": " + A);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadPool.shutdown();
    }
}
