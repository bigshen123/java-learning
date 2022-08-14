package com.bigshen.learningDemo.leetcode.queue;

/**
 * @Description: 圆环队列
 * @Author: BIGSHEN
 * @Date: 2019/12/21 17:47
 */
public class CircularQueue {
    /**数组：items*/
    private String[] items;
    /**数组大小n*/
    private int n;
    /**tail表示队尾下标*/
    private int tail = 0;
    /**head表示队头下标*/
    private int head = 0;

    /**
     申请一个大小为capacity的数组
     */
    public CircularQueue(int capacity) {
        items = new String[capacity];
        n = capacity;
    }

    /**
     入队
     */
    public boolean enqueue(String item) {
        // 队列满了
        if ((tail + 1) % n == head) {
            return false;
        }
        items[tail] = item;
        tail = (tail + 1) % n;
        return true;
    }

    /**
     出队
     */
    public String dequeue() {
        // 如果head == tail 表示队列为空
        if (head == tail) {
            return null;
        }
        String ret = items[head];
        head = (head + 1) % n;
        return ret;
    }

    public void printAll() {
        if (0 == n) {
            return;
        }
        for (int i = head; i % n != tail; ++i) {
            System.out.print(items[i] + " ");
        }
        System.out.println();
    }
}