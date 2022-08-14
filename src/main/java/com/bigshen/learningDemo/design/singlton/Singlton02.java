package com.bigshen.learningDemo.design.singlton;

/**
 * @ClassName Singlton02
 * @Description:TODO 饿汉式
 * @Author: byj
 * @Date: 2020/12/1
 */
public class Singlton02 {

    private static Singlton02 singlton02=new Singlton02();

    private Singlton02(){
        System.out.println("私有Singlton02构造参数初始化");
    }
    public static Singlton02 getInstance(){
        return singlton02;
    }

    public static void main(String[] args) {
        Singlton02 instance1 = Singlton02.getInstance();
        Singlton02 instance2 = Singlton02.getInstance();
        System.out.println(instance1==instance2);

    }
}
