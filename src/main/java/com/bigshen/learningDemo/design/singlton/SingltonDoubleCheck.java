package com.bigshen.learningDemo.design.singlton;



/**
 * @ClassName Singlton05
 * @Description:TODO 双重检测锁（因为JVM本质重排序的原因，可能会初始化多次，不推荐使用）
 * 双重锁的方式是方法级锁的优化，减少了部分获取实例的耗时。
 * 同时这种方式也满足了懒加载。
 * @Author: byj
 * @Date: 2020/12/1
 */
public class SingltonDoubleCheck {

    /**
     这里的 volatile 关键字主要是为了防止指令重排
     */
    private static volatile SingltonDoubleCheck singlton05;

    private SingltonDoubleCheck(){
        System.out.println("私有Singlton05构造参数初始化");
    }
    public static SingltonDoubleCheck getInstance(){
        if (singlton05==null){
            synchronized (SingltonDoubleCheck.class){
                if (singlton05==null){
                    singlton05=new SingltonDoubleCheck();
                }
            }
        }
        return singlton05;
    }

    public static void main(String[] args) {
        SingltonDoubleCheck instance1 = SingltonDoubleCheck.getInstance();
        SingltonDoubleCheck instance2 = SingltonDoubleCheck.getInstance();
        System.out.println(instance1==instance2);
    }
}
