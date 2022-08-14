package com.bigshen.learningDemo.design.observer;

/**
 * @Description: 二进制观察者实体
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:26
 */
public class BinaryObserver extends Observer {

    BinaryObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }
    @Override
    public void update() {
        System.out.println( "Binary String: "
                + Integer.toBinaryString( subject.getState() ) );
    }
}
