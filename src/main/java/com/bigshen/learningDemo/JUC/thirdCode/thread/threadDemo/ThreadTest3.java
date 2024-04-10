package com.bigshen.learningDemo.JUC.thirdCode.thread.threadDemo;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/15 19:04
 */
public class ThreadTest3 implements Runnable {
    @Override
    public void run() {
        try {
            for (int i = 0; i < 10000; i++) {
                synchronized (ThreadMain.class) {
                    ThreadTest2.j--;
                    System.out.println("线程3=====" + ThreadTest2.j);
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
