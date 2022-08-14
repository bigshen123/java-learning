package com.bigshen.learningDemo.design.template.lockTemplate;

import java.util.concurrent.CountDownLatch;

/**
 * @Author BYJ
 * @Date 2022/5/6 14:52
 * @Describe
 */
public class ZkDistributedLock extends ZkAbstractTemplateLock {
    @Override
    protected void waitZkLock() {
        // 等待锁的时候，需要加监控，查询这个lock是否释放

        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 解除监听
    }

    @Override
    protected boolean tryLock() {
        // 判断节点是否存在，如果存在则返回false，否者返回true
        return false;
    }
}
