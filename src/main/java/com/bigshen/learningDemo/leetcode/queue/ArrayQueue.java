package com.bigshen.learningDemo.leetcode.queue;

/**
 * @Description:用数组实现队列
 * @Author: BIGSHEN
 * @Date: 2019/12/21 17:16
 */
public class ArrayQueue {

    //数组
    private String[] items;
    //数组大小
    private int n = 0;
    //队头下标
    private int head = 0;
    //队尾下标
    private int tail = 0;

    /**
     * 申请一个大小为capacity的数组
     *
     * @param capacity 数组大小
     */
    public ArrayQueue(int capacity) {
        items = new String[capacity];
        n = capacity;
    }

    /**
     * 入队
     *
     * @param item 入队元素
     * @return
     */
    public boolean enqueue(String item) {
        //如果tail==n,表示队列已满
        if (tail == n) return false;
        items[tail] = item;
        ++tail;
        return true;
    }

    /**
     * 出队
     *
     * @return
     */
    public String dequeue() {
        if (head == tail) return null;
        String ret = items[head];
        ++head;
        return ret;
    }

    public void printAll() {
        for (int i = head; i < tail; ++i) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }
}
