package com.bigshen.learningDemo.JUC.pool.threadPoolDemo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author byj
 * @date 2022/11/7
 */
public class AsyncTest {

    @Autowired
    private AsyncTestImpl asyncTest;

    @Test
    public void testAsync() throws Exception {
        asyncTest.doTaskOne();
        asyncTest.doTaskTwo();
        asyncTest.doTaskThree();
        asyncTest.doTaskOne();
        Thread.currentThread().join();//等上面的三个线程都执行完毕，才执行当前主线程，以确保上面的三个线程都能够执行完毕
    }
}
