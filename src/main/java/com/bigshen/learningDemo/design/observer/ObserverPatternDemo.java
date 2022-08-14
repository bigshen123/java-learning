package com.bigshen.learningDemo.design.observer;

/**
 * @Description:观察者模式demo
 * 适用于一对多关系中
 * 一个对象修改通知其他对象一并修改
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:30
 */
public class ObserverPatternDemo {
    public static void main(String[] args) {
        Subject subject = new Subject();

        new HexaObserver(subject);
        new OctalObserver(subject);
        new BinaryObserver(subject);

        System.out.println("First state change: 15");
        subject.setState(15);
        System.out.println("Second state change: 10");
        subject.setState(10);
    }
}
