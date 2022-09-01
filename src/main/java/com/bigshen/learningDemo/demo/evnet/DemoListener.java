package com.bigshen.learningDemo.demo.evnet;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Author BYJ
 * @Date 2022/9/1 20:49
 * @Describe 事件监听器
 */
@Component
public class DemoListener implements ApplicationListener<DemoEvent> { //实现ApplicationListener接口，并指定监听的事件类型
    @Override
    public void onApplicationEvent(DemoEvent event) { //使用onApplicationEvent方法对消息进行接受处理
        String msg = event.getMsg();
        System.out.println("DemoListener获取到了监听消息:"+msg);
    }
}
