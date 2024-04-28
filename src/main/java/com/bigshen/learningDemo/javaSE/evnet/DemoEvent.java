package com.bigshen.learningDemo.javaSE.evnet;

import org.springframework.context.ApplicationEvent;

/**
 * @Author BYJ
 * @Date 2022/9/1 20:50
 * @Describe 自定义事件
 * Spring提供了以下5种标准的事件：
 * <p>
 * 上下文更新事件（ContextRefreshedEvent）：在调用ConfigurableApplicationContext 接口中的refresh()方法时被触发。
 * 上下文开始事件（ContextStartedEvent）：当容器调用ConfigurableApplicationContext的Start()方法开始/重新开始容器时触发该事件。
 * 上下文停止事件（ContextStoppedEvent）：当容器调用ConfigurableApplicationContext的Stop()方法停止容器时触发该事件。
 * 上下文关闭事件（ContextClosedEvent）：当ApplicationContext被关闭时触发该事件。容器被关闭时，其管理的所有单例Bean都被销毁。
 * 请求处理事件（RequestHandledEvent）：在Web应用中，当一个http请求（request）结束触发该事件。
 * 如果一个bean实现了ApplicationListener接口，当一个ApplicationEvent被发布以后，bean会自动被通知
 * <p/>
 */
public class DemoEvent extends ApplicationEvent {
    private String msg;

    public DemoEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
