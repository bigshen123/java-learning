package com.bigshen.learningDemo.demo.limit;



import org.apache.curator.shaded.com.google.common.util.concurrent.RateLimiter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2024/1/5
 * @Description
 */
public class APILimiter {
    private static final int MAX_REQUESTS_PER_SECOND = 5;
    private final static RateLimiter rateLimiter = RateLimiter.create(MAX_REQUESTS_PER_SECOND);

    public static void main(String[] args) {
        // 创建线程池，模拟并发请求
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 创建多个线程，每个线程都会不断地调用 canProcessRequest 方法
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 100; j++) {
                    boolean acquire = rateLimiter.tryAcquire(200, TimeUnit.MILLISECONDS);
                    if (acquire) {
                        //获取令牌成功
                        System.out.println("接口访问成功，处理业务逻辑..." + LocalDateTime.now());
                    } else {
                        System.out.println("接口访问受限，稍后重试..."+ LocalDateTime.now());
                    }
                }
            });
        }
        // 关闭线程池
        executorService.shutdown();
    }
}
