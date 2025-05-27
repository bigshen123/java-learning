package com.bigshen.learningDemo.design.singlton;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description: 用CAS的好处在于不需要使用传统的锁机制来保证线程安全, CAS是一种基于忙等待的算法, 依赖底层硬件的实现,
 * 相对于锁它没有线程切换和阻塞的额外消耗, 可以支持较大的并行度。
 * <p>
 * CAS的一个重要缺点在于如果忙等待一直执行不成功(一直在死循环中),会对CPU造成较大的执行开销。
 * <p>
 * 另外，如果N个线程同时执行到singleton = new Singleton();的时候，会有大量对象创建，很可能导致内存溢出。
 * @Author: BIGSHEN
 * @Date: 2019/12/20 20:02
 */
public class SingletonCas {
    private static final AtomicReference<SingletonCas> instance = new AtomicReference<>();

    private SingletonCas() {

    }

    public static SingletonCas getInstance() {
        for (; ; ) {
            SingletonCas singleton = instance.get();
            if (singleton != null) {
                return singleton;
            }
            //高并发下创建大量对象 可能导致内存溢出
            singleton = new SingletonCas();
            //如果当前值==期望值，则自动将该值设置为给定的更新值
            if (instance.compareAndSet(null, singleton)) {
                return singleton;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(SingletonCas.getInstance());
        System.out.println(SingletonCas.getInstance());
    }
}
