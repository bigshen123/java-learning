package com.bigshen.learningDemo.design.observer;

/**
 * @Description:观察者
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:25
 */
public abstract class Observer {
    protected Subject subject;
    public abstract void update();
}
