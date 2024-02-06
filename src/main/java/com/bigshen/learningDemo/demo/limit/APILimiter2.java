package com.bigshen.learningDemo.demo.limit;

import java.time.LocalDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author byj
 * @date 2024/1/5
 * @Description
 */
public class APILimiter2 {
    private static final int MAX_REQUESTS_PER_SECOND = 5;
    private static final AtomicInteger requestCount = new AtomicInteger(0);

    public static void main(String[] args) {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                requestCount.set(0); // 重置请求计数器
            }
        }, 0, 1000); // 每秒重置计数器

        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    if (canProcessRequest()) {
                        System.out.println("Request can be processed: " + LocalDateTime.now());
                    } else {
                        System.out.println("Request rate limit exceeded: " + LocalDateTime.now());
                    }
                    try {
                        Thread.sleep(200); // 模拟每个请求之间的间隔
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static boolean canProcessRequest() {
        return requestCount.incrementAndGet() <= MAX_REQUESTS_PER_SECOND;
    }
}
