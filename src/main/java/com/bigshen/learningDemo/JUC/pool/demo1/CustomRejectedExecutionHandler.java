package com.bigshen.learningDemo.JUC.pool.demo1;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author BYJ
 * @Date 2024/4/14 11:53
 * @Describe 自定义拒绝策略
 */
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        // 尝试将任务添加到线程池的队列中，直到成功或线程池被关闭
        try {
            executor.getQueue().put(r);
        } catch (InterruptedException e) {
            // 如果线程被中断，则打印异常信息
            throw new RuntimeException(e);
        }
    }
}
