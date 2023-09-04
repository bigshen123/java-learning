package com.bigshen.learningDemo.design.singlton;

/**
 * @Description: 懒汉式 线程不安全
 * @Author: BIGSHEN
 * @Date: 2019/12/13 10:56
 */
public class SingltonLazy {
    private static volatile SingltonLazy singlton;

    private SingltonLazy() {
        System.out.println("私有Singlton构造参数初始化");
    }

    private static synchronized SingltonLazy getInstance() {
        if (singlton == null) {
            singlton = new SingltonLazy();
        }
        return  singlton;
    }

    public static void main(String[] args) {
        SingltonLazy singlton1 = SingltonLazy.getInstance();
        SingltonLazy singlton2 = SingltonLazy.getInstance();
        System.out.println(singlton1==singlton2);
    }
}
