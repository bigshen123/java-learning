package com.bigshen.learningDemo.JUC.thirdCode.thread.threadDemo;

/**
 * @Description:设计4个线程，其中两个线程每次对j增加1，另外两个线程对j每次减少1。
 * 考虑到线程的安全性写出程序。
 * 分别书写四个线程对静态变量j进行更改，在每个线程中都加入synchronized (ThreadTest.class)保证线程安全，
 * 在更改未做完其他线程无法运行
 * @Author: BIGSHEN
 * @Date: 2019/12/15 19:05
 */
public class ThreadMain {
    public static void main(String[] args) {
        new Thread(new ThreadTest1()).start();
        new Thread(new ThreadTest2()).start();
        new Thread(new ThreadTest3()).start();
        new Thread(new ThreadTest4()).start();

    }
}
