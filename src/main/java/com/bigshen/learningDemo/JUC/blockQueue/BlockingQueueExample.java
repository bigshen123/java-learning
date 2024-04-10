package com.bigshen.learningDemo.JUC.blockQueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author BYJ
 * @Date 2022/12/26 16:49
 * @Describe
 */
public class BlockingQueueExample {
    public static void main(String[] args) throws Exception {

        BlockingQueue<String> queue = new ArrayBlockingQueue<>(1024);

        Producer producer = new Producer(queue);

        Consumer consumer = new Consumer(queue);

        new Thread(producer).start();

        new Thread(consumer).start();

        Thread.sleep(4000);

    }
}
