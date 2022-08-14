package com.bigshen.learningDemo.concurrent.thread.guitu;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/15 18:47
 */
public class ThreadTest1 implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 1; i <= 100; i+=5) {
                System.out.println("乌龟跑了"+i+"米");
                Thread.sleep(1000);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }
}
