package com.bigshen.learningDemo.javaSE.limit;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author byj
 * @date 2024/1/5
 * @Description
 */
public class TokenBucketRateLimiter {
    private final int capacity;
    private final int refillRate;
    private final long interval;
    private final TimeUnit timeUnit;
    private final Semaphore semaphore;
    private final ScheduledExecutorService scheduler;
    private final AtomicInteger threadCounter = new AtomicInteger(1);
    private volatile boolean isRunning;



    /**
     * 获取一个接口限速器
     *
     * @param capacity   令牌桶容量
     * @param interval   间隔，和 timeUnit 对应
     * @param timeUnit   时间单位
     * @param refillRate 每 interval 添加几个令牌
     */
    public TokenBucketRateLimiter(int capacity, long interval, TimeUnit timeUnit, int refillRate) {
        this.semaphore = new Semaphore(0);
        this.refillRate = refillRate;
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.capacity = capacity;
        ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r, "TokenRefillThread-" + threadCounter.getAndIncrement());
            thread.setDaemon(true);
            return thread;
        };
        this.scheduler = new ScheduledThreadPoolExecutor(1, threadFactory);
        this.isRunning = false;
    }

    public boolean allowRequest() {
        return semaphore.tryAcquire();
    }

    public void start() {
        if (!isRunning) {
            scheduler.scheduleAtFixedRate(this::refillTokens, 0, interval, timeUnit);
            isRunning = true;
        }
    }

    public void stop() {
        if (isRunning) {
            scheduler.shutdown();
            isRunning = false;
        }
    }

    private void refillTokens() {
        if (semaphore.availablePermits() < capacity) {
            semaphore.release(Math.min(refillRate, capacity - semaphore.availablePermits()));
        }
    }

    public static void main(String[] args) {

        TokenBucketRateLimiter tokenBucketRateLimiter = new TokenBucketRateLimiter(5, 1, TimeUnit.SECONDS, 5);
        tokenBucketRateLimiter.start();
        // 创建线程池，模拟并发请求
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        // 创建多个线程，每个线程都会不断地调用 canProcessRequest 方法
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 1000; j++) {
                    boolean acquire = tokenBucketRateLimiter.allowRequest();
                    if (acquire) {
                        //获取令牌成功
                        System.out.println("接口访问成功，处理业务逻辑..." + LocalDateTime.now());
                    } else {
                        System.out.println("接口访问受限，稍后重试..."+ LocalDateTime.now());
                    }
                    try {
                        TimeUnit.MICROSECONDS.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
        // 关闭线程池
        executorService.shutdown();
        tokenBucketRateLimiter.stop();
    }
}
