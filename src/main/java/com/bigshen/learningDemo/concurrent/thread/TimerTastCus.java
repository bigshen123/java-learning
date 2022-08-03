package com.bigshen.learningDemo.concurrent.thread;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @Description: 双重定时器
 * 使用定时器,间隔 4 秒执行一次，再间隔 2 秒执行一次，以此类推执行
 * @Author: BIGSHEN
 * @Date: 2019/12/22 9:37
 */
public class TimerTastCus extends TimerTask {

    private static volatile int count=0;

    @Override
    public void run() {
        //1 0 1 0 1 0
        count=(count+1)%2;
        System.out.println("Boom.....Boom.....");
        new Timer().schedule(new TimerTastCus(),2000+2000*count);
    }

    public static void main(String[] args) {
        Timer timer=new Timer();
        timer.schedule(new TimerTastCus(),2000+count*2000);
        while (true){
            System.out.println(System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
