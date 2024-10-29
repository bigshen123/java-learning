package com.bigshen.learningDemo.JUC.lockSupport;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author BYJ
 * @Date 2024/10/29 20:25
 * @Describe 中断响应demo
 *
 * 在主线程调用park阻塞后，在myThread线程中发出了中断信号，此时主线程会继续运行，也就是说明此时interrupt起到的作用与unpark一样
 */
class MyThread2 extends Thread {
    private Object object;

    public MyThread2(Object object) {
        this.object = object;
    }

    @Override
    public void run() {
        System.out.println("before interrupt");
        try {
            // 休眠3s
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Thread thread = (Thread) object;
        // 中断线程
        thread.interrupt();
        System.out.println("after interrupt");
    }
}

public class InterruptDemo {
    public static void main(String[] args) {
        MyThread2 myThread = new MyThread2(Thread.currentThread());
        myThread.start();
        System.out.println("before park");
        // 获取许可
        LockSupport.park("ParkAndUnparkDemo");
        System.out.println("after park");
    }
}
