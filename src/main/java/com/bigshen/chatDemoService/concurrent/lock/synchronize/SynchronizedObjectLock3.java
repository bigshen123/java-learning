package com.bigshen.chatDemoService.concurrent.lock.synchronize;

/**
 * @Author BYJ
 * @Date 2022/6/1 15:54
 * @Describe
 */
public class SynchronizedObjectLock3 implements Runnable{

    static SynchronizedObjectLock3 instence1 = new SynchronizedObjectLock3();
    static SynchronizedObjectLock3 instence2 = new SynchronizedObjectLock3();

    @Override
    public void run() {
        //method();
        method2();
    }

    /**
     synchronized用在普通方法上，默认的锁就是this，当前实例
     */
    public synchronized void method() {
        System.out.println("我是线程" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "结束");
    }

    /**
     synchronized用在静态方法上，默认的锁就是当前所在的Class类，所以无论是哪个线程访问它，需要的锁都只有一把
     */
    public static synchronized void method2() {
        System.out.println("我是线程" + Thread.currentThread().getName());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + "结束");
    }

    public static void main(String[] args) {
        // t1和t2对应的this是两个不同的实例，所以代码不会串行
        Thread t1 = new Thread(instence1);
        Thread t2 = new Thread(instence2);
        t1.start();
        t2.start();
    }
}
