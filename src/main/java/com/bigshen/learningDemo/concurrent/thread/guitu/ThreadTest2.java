package com.bigshen.learningDemo.concurrent.thread.guitu;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/15 18:47
 */
public class ThreadTest2 implements Runnable {

    @Override
    public void run() {
        try {
            for(int j = 1;j<=100;j=j+10){
                System.out.println("兔子跑了"+j+"米");
                Thread.sleep(1000);
                if(j==51){
                    Thread.sleep(1500);
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
