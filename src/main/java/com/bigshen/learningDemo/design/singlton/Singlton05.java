package com.bigshen.learningDemo.design.singlton;



/**
 * @ClassName Singlton05
 * @Description:TODO 双重检测锁（因为JVM本质重排序的原因，可能会初始化多次，不推荐使用）
 * @Author: byj
 * @Date: 2020/12/1
 */
public class Singlton05 {

    /**
     这里的 volatile 关键字主要是为了防止指令重排
     */
    private static volatile Singlton05 singlton05;

    private Singlton05(){
        System.out.println("私有Singlton05构造参数初始化");
    }
    public static Singlton05 getInstance(){
        if (singlton05==null){
            synchronized (Singlton05.class){
                if (singlton05==null){
                    singlton05=new Singlton05();
                }
            }
        }
        return singlton05;
    }

    public static void main(String[] args) {
        Singlton05 instance1 = Singlton05.getInstance();
        Singlton05 instance2 = Singlton05.getInstance();
        System.out.println(instance1==instance2);
    }
}
