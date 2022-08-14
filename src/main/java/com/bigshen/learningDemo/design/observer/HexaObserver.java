package com.bigshen.learningDemo.design.observer;

/**
 * @Description: 十六进制观察者实体
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:29
 */

public class HexaObserver extends Observer{

    public HexaObserver(Subject subject){
        this.subject = subject;
        this.subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println( "Hex String: "
                + Integer.toHexString( subject.getState() ).toUpperCase() );
    }
}