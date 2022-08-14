package com.bigshen.learningDemo.concurrent.Volatile;

import java.util.concurrent.TimeUnit;

/**
 * @Author BYJ
 * @Date 2020/12/17 22:07
 * @Describe 这里的 flag 存放于主内存中，所以主线程和线程 A 都可以看到。
 *          flag 采用 volatile 修饰主要是为了内存可见性，
 */
public class Volatile implements Runnable{
    //使用volatile修饰 直接刷新主内存中，保证内存可见性
    private static volatile boolean flag = true ;

    @Override
    public void run() {
        while (flag){
            System.out.println(Thread.currentThread().getName()+"正在运行。。。。。。");
        }
        System.out.println(Thread.currentThread().getName()+"执行完成。。。。。。");
    }

    public static void main(String[] args) throws InterruptedException {
        Volatile v=new Volatile();
        new Thread(v,"thread A").start();
        System.out.println("main 线程正在运行");
        TimeUnit.MILLISECONDS.sleep(100);
        v.stopThread();
    }
    private void stopThread(){
        flag = false ;
    }
}
