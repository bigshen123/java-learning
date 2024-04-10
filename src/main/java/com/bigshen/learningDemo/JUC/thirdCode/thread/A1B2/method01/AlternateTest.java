package com.bigshen.learningDemo.JUC.thirdCode.thread.A1B2.method01;

/**
 * @Author BYJ
 * @Date 2020/12/16 22:04
 * @Describe 交替的输出A1B2C3D4 a线程输出字母，b线程输出数字 要求a线程先执行
 * sync+wait notify实现
 */
public class AlternateTest {
    public static void main(String[] args) {
        final Object lock=new Object();
        Thread1 thread1=new Thread1(lock);
        Thread2 thread2=new Thread2(lock);
        thread1.start();
        thread2.start();
    }
}
