package com.bigshen.learningDemo.design.singlton;

import java.time.Instant;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Description:
 * 用CAS的好处在于不需要使用传统的锁机制来保证线程安全, CAS是一种基于忙等待的算法, 依赖底层硬件的实现,
 * 相对于锁它没有线程切换和阻塞的额外消耗, 可以支持较大的并行度。
 *
 * CAS的一个重要缺点在于如果忙等待一直执行不成功(一直在死循环中),会对CPU造成较大的执行开销。
 *
 * 另外，如果N个线程同时执行到singleton = new Singleton();的时候，会有大量对象创建，很可能导致内存溢出。
 * @Author: BIGSHEN
 * @Date: 2019/12/20 20:02
 */
public class Singleton {
    private static final AtomicReference<Singleton> instance = new AtomicReference<>();

    private Singleton() {

    }

    public static Singleton getInstance() {
        for (; ; ) {
            Singleton singleton = instance.get();
            if (singleton != null) {
                return singleton;
            }
            //高并发下创建大量对象 可能导致内存溢出
            singleton = new Singleton();
            //如果当前值==期望值，则自动将该值设置为给定的更新值
            if (instance.compareAndSet(null, singleton)) {
                return singleton;
            }
        }
    }

    public static void main(String[] args) {
        Instant now = Instant.now();
        System.out.println(now);
        Instant instant = now.minusSeconds(TimeUnit.SECONDS.toSeconds(10));
        System.out.println(instant);
    }
}
