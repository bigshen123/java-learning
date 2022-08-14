package com.bigshen.learningDemo.design.observer;

import java.util.ArrayList;
import java.util.List;



/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:24
 */
class Subject {
    /**
     * 装载所有观察者
     */
    private List<Observer> observers = new ArrayList<Observer>();
    private int state;

    int getState() {
        return state;
    }

    /**
     * 值被修改则通知所有观察者
     * @param state
     */
    void setState(int state) {
        this.state = state;
        notifyAllObservers();
    }

    /**
     * 添加观察者
     * @param observer
     */
    void attach(Observer observer){
        observers.add(observer);
    }

    /**
     * 通知所有观察者实体值被修改了
     */
    private void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }
}
