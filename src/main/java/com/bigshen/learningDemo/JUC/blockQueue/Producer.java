package com.bigshen.learningDemo.JUC.blockQueue;

import java.util.concurrent.BlockingQueue;

/**
 * @Author BYJ
 * @Date 2022/12/26 16:50
 * @Describe 生产者
 */
public class Producer implements Runnable {


    protected BlockingQueue<String> queue;


    public Producer(BlockingQueue<String> queue) {

        this.queue = queue;

    }


    @Override
    public void run() {

        try {

            queue.put("1");
            System.out.println("生产1完成");

            Thread.sleep(1000);

            queue.put("2");
            System.out.println("生产2完成");

            Thread.sleep(1000);

            queue.put("3");
            System.out.println("生产3完成");

        } catch (InterruptedException e) {

            e.printStackTrace();

        }

    }

}


