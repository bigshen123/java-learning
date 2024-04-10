package com.bigshen.learningDemo.JUC.blockQueue;

import java.util.concurrent.BlockingQueue;

/**
 * @Author BYJ
 * @Date 2022/12/26 16:51
 * @Describe 消费者
 */
public class Consumer implements Runnable {


    protected BlockingQueue<String> queue;


    public Consumer(BlockingQueue<String> queue) {

        this.queue = queue;

    }


    @Override
    public void run() {

        try {

            System.out.println("消费：" + queue.take());

            System.out.println("消费：" + queue.take());

            System.out.println("消费：" + queue.take());

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

}