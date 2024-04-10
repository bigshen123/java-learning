package com.bigshen.learningDemo.JUC.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/4 14:07
 */
public class MyPool {
    //池相关的信号量
    private Semaphore sp;

    /**
     * 池的大小，这个大小会传递给信号量
     *
     * @param size 池的大小
     */
    MyPool(int size) {
        this.sp = new Semaphore(size);
    }

    public Semaphore getSp() {
        return sp;
    }

    public void setSp(Semaphore sp) {
        this.sp = sp;
    }
}
