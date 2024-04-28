package com.bigshen.learningDemo.javaSE.evnet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @Author BYJ
 * @Date 2022/9/1 20:49
 * @Describe 事件发布类
 */
@Component
public class DemoPublisher {

    @Autowired
    private ApplicationContext applicationContext;

    public void publish(String msg){
        //使用ApplicationContext对象的publishEvent发布事件
        applicationContext.publishEvent(new DemoEvent(this,msg));
    }
}
