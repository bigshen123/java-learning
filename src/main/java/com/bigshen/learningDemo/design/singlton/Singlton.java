package com.bigshen.learningDemo.design.singlton;

/**
 * @Description: 懒汉式
 * @Author: BIGSHEN
 * @Date: 2019/12/13 10:56
 */
public class Singlton {
    private static volatile Singlton singlton;

    private Singlton() {
        System.out.println("私有Singlton构造参数初始化");
    }

    private static synchronized Singlton getInstance() {
        if (singlton == null) {
            singlton = new Singlton();
        }
        return  singlton;
    }

    public static void main(String[] args) {
        Singlton singlton1 = Singlton.getInstance();
        Singlton singlton2 = Singlton.getInstance();
        System.out.println(singlton1==singlton2);
    }
}
