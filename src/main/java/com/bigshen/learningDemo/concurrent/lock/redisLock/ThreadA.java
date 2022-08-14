package com.bigshen.learningDemo.concurrent.lock.redisLock;

/**
 * @ClassName ThreadA
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/11
 */
public class ThreadA extends Thread {
    private Service service;

    public ThreadA(Service service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.seckill();
    }
}




