package com.bigshen.learningDemo.JUC.lock.redisLock;

/**
 * @ClassName Test
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/11
 */
public class Test {
    public static void main(String[] args) {
        Service service = new Service();
        for (int i = 0; i < 50; i++) {
            ThreadA threadA = new ThreadA(service);
            threadA.start();
        }
    }
}
