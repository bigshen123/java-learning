package com.bigshen.learningDemo.concurrent.thread.threadLocal;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/22 10:21
 */
public class SerialNum {
    private static int nextSerialNum = 0;
    private static ThreadLocal serialNum = new ThreadLocal() {
        protected synchronized Object initialValue() {
            return nextSerialNum++;
        }
    };
    public static int get() {
        return (Integer) (serialNum.get());
    }
}
