package com.bigshen.chatDemoService.concurrent.lock.synchronize;

/**
 * @Author BYJ
 * @Date 2022/6/1 15:47
 * @Describe
 */
public class SynchronizedObjectLock2 implements Runnable {

    static SynchronizedObjectLock2 INSTANCE = new SynchronizedObjectLock2();
    /**
     创建2把锁
     *
     */
    final Object block1 = new Object();
    final Object block2 = new Object();

    @Override
    public void run() {
        // 这个代码块使用的是第一把锁，当他释放后，后面的代码块由于使用的是第二把锁，因此可以马上执行
        synchronized (block1) {
            System.out.println("block1锁,我是线程" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("block1锁," + Thread.currentThread().getName() + "结束");
        }

        synchronized (block2) {
            System.out.println("block2锁,我是线程" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("block2锁," + Thread.currentThread().getName() + "结束");
        }
    }

    public static void main(String[] args) {
        Thread t1 = new Thread(INSTANCE);
        Thread t2 = new Thread(INSTANCE);
        t1.start();
        t2.start();
    }
}
