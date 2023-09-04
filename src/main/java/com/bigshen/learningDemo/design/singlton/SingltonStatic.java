package com.bigshen.learningDemo.design.singlton;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName Singlton03
 * @Description:TODO 静态内部类 结合了懒汉、饿汉的优点，真正需要对象时再加载，加载类是线程安全的
 * @Author: byj
 * @Date: 2020/12/1
 */
public class SingltonStatic {

    public static Map<String,String> cache = new ConcurrentHashMap<>();

    private SingltonStatic() {
        System.out.println("私有Demo3构造参数初始化");
    }

    public static class SingltonClassInstance{
        private static final SingltonStatic singlton03=new SingltonStatic();


    }

    public static SingltonStatic getInstance(){
        return SingltonClassInstance.singlton03;
    }

    public static void main(String[] args) {
        SingltonStatic instance1 = SingltonStatic.getInstance();
        SingltonStatic instance2 = SingltonStatic.getInstance();
        System.out.println(instance1==instance2);
    }

}
