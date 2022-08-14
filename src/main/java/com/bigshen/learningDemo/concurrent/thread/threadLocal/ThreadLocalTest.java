package com.bigshen.learningDemo.concurrent.thread.threadLocal;

import java.util.Random;

/**
 * @Description: 在runnable中创建ThreadLocal保证多线程中线程安全
 * @Author: BIGSHEN
 * @Date: 2019/12/22 10:56
 */
public class ThreadLocalTest implements Runnable {
    private ThreadLocal<Student> studentThreadLocal=new ThreadLocal<Student>();
    @Override
    public void run() {
        String currentThreadName = Thread.currentThread().getName();
        System.out.println(currentThreadName + " is running...");
        Random random = new Random();
        int age = random.nextInt(100);
        System.out.println(currentThreadName + " is set age: " + age);
        Student studen = getStudent(); //通过这个方法，为每个线程都独立的 new 一个 student 对象，
        // 每个线程的student 对象都可以设置不同的值
        studen.setAge(age);
        System.out.println(currentThreadName + " is first get age: " + studen.getAge());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println( currentThreadName + " is second get age: " + studen.getAge());
    }
    private Student getStudent() {
        Student studen = studentThreadLocal.get();
        if (null == studen) {
            studen = new Student();
            studentThreadLocal.set(studen);
        }
        return studen;
    }
    public static void main(String[] args) {
        ThreadLocalTest t = new ThreadLocalTest();
        Thread t1 = new Thread(t,"Thread A");
        Thread t2 = new Thread(t,"Thread B");
        t1.start();
        t2.start();
    }
}
