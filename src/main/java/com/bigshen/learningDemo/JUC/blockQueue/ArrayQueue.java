package com.bigshen.learningDemo.JUC.blockQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:数组实现的线程安全的阻塞队列
 * @Author: BIGSHEN
 * @Date: 2019/12/24 18:18
 */
public final class ArrayQueue<T> {

    /**
     * 最终的数据存储
     */
    private final Object[] items;
    /**
     * 队列数量
     */
    private int count = 0;
    /**
     * 写入数据时的下标
     */
    private int putIndex;
    /**
     * 获取数据时的下标
     */
    private int getIndex;

    private final Lock lock = new ReentrantLock();
    /**
     * 队列满时的等待条件
     */
    private final Condition notFull = lock.newCondition();
    /**
     * 队列空时的等待条件
     */
    private final Condition notEmpty = lock.newCondition();

    public ArrayQueue(int size) {
        items = new Object[size];
    }

    /**
     * 从队列尾写入数据
     */
    public void put(T t) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                // 队列满时等待
                notFull.await();
            }
            items[putIndex] = t;
            putIndex = (putIndex + 1) % items.length;
            count++;
            // 唤醒等待获取数据的线程
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从队列头获取数据
     */
    public T get() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                // 队列空时等待
                notEmpty.await();
            }
            T result = (T) items[getIndex];
            items[getIndex] = null;
            getIndex = (getIndex + 1) % items.length;
            count--;
            // 唤醒等待放入数据的线程
            notFull.signal();
            return result;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 获取队列大小
     */
    public int size() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 判断队列是否为空
     */
    public boolean isEmpty() {
        return size() == 0;
    }


    public static void main(String[] args) {
        ArrayQueue<Integer> queue = new ArrayQueue<>(5);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // 生产者线程
        executor.execute(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    queue.put(i);
                    System.out.println("Produced: " + i);
                    Thread.sleep(500); // 模拟生产间隔
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 消费者线程
        executor.execute(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    int value = queue.get();
                    System.out.println("Consumed: " + value);
                    Thread.sleep(1000); // 模拟消费间隔
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        executor.shutdown();
    }
}
