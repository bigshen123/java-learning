package com.bigshen.learningDemo.concurrent.thread.A1B2.method01;

/**
 * @Author BYJ
 * @Date 2020/12/16 22:03
 * @Describe
 */
public class Thread1 extends Thread {

    //定义锁对象，负责接收传入对象
    private final Object lock;

    public Thread1(Object lock){
        this.lock = lock;
    }

    @Override
    public void run(){
        String[] string = {"A", "B", "C", "D"};
        for (String s : string) {
            synchronized (lock) {
                //线程1开始执行
                lock.notify();
                try {
                    //线程1开始等待
                    lock.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //线程1继续执行
                System.out.print(s);
                lock.notify();
            }
        }
    }
}
