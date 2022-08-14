package com.bigshen.learningDemo.concurrent.thread.threadDemo;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/15 19:04
 */
public class ThreadTest2 implements Runnable {
    public static int j=500;
    public static int k;
    @Override
    public void run(){

        try {
            for(int i=0;i<10000;i++){
                synchronized (ThreadMain.class){
                    j++;
                    System.out.println("线程2====="+j);
                    Thread.sleep(100);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
