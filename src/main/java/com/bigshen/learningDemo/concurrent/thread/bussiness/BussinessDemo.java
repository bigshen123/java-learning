package com.bigshen.learningDemo.concurrent.thread.bussiness;

/**
 * @Description:子线程运行执行 10 次后，主线程再运行 5 次。这样交替执行三遍
 * @Author: BIGSHEN
 * @Date: 2019/12/22 10:07
 */
public class BussinessDemo {
    public static void main(String[] args) {
        final Bussiness bussiness = new Bussiness();
        //子线程
        new Thread(() -> {
            for (int i = 0; i < 3; i++) {
                bussiness.subMethod();
            }
        }).start();
        //主线程
        for (int i = 0; i < 3; i++) {
            bussiness.mainMethod();
        }
    }
}
