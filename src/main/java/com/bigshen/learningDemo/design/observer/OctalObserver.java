package com.bigshen.learningDemo.design.observer;

/**
 * @Description:八进制观察者实体
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:28
 */
public class OctalObserver extends Observer{

    public OctalObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println( "Octal String: "
                + Integer.toOctalString( subject.getState() ) );
    }
}
