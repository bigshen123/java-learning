package com.bigshen.chatDemoService.demo.test;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/5 20:52
 */
public class SingletonDemo {
    private SingletonDemo(){
    }

    //”急切“或”饿汉“式（线程安全，因为JVM加载此类时立即创建此类的唯一实例）
    private final static SingletonDemo instance=new SingletonDemo();
    public static SingletonDemo getInstance(){
        return instance;
    }

    //最简单的写法（非线程安全，有叫它“懒汉式”的）
    private static SingletonDemo instance2 = null;
    public static SingletonDemo getInstance2(){
        if (instance==null){
            instance2=new SingletonDemo();
        }
        return instance2;
    }

    //加”synchronized“保证多线程下的线程安全（同步代码块，高频访问时，性能较差）
    private static SingletonDemo uniqueInstance;
    public static synchronized SingletonDemo getInstance3(){
        if (uniqueInstance==null){
            uniqueInstance=new SingletonDemo();
        }
        return uniqueInstance;
    }

    //”双重检查加锁“ （面试常问）
    // 注：第一次检查，如果实例不存在，则进入同步块；进入同步块后再次检查，
    // 防止第二个线程在第一个线程执行第二次检查之前，
    // 已经创建了一个实例。volatile 关键字将禁止指令重排序。
    private volatile static SingletonDemo uniqueInstance2;
    public static SingletonDemo getInstance4(){
        if (uniqueInstance2==null){
            //全局锁
            synchronized (SingletonDemo.class){
                if (uniqueInstance2==null){
                    uniqueInstance2=new SingletonDemo();
                }
            }
        }
        return uniqueInstance2;
    }



}
