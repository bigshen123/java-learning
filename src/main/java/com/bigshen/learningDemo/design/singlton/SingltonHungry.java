package com.bigshen.learningDemo.design.singlton;

/**
 * @ClassName Singlton02
 * @Description:TODO 饿汉式
 * @Author: byj
 * @Date: 2020/12/1
 */
public class SingltonHungry {

    private static SingltonHungry singlton02=new SingltonHungry();

    private SingltonHungry(){
        System.out.println("私有Singlton02构造参数初始化");
    }
    public static SingltonHungry getInstance(){
        return singlton02;
    }

    public static void main(String[] args) {
        SingltonHungry instance1 = SingltonHungry.getInstance();
        SingltonHungry instance2 = SingltonHungry.getInstance();
        System.out.println(instance1==instance2);

    }
}
