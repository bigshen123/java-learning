package com.bigshen.learningDemo.javaSE.evnet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author BYJ
 * @Date 2022/9/1 20:48
 * @Describe
 */
public class App {
    public static void main(String[] args) {
        //使用AnnotationConfigApplicationContext读取配置EventConfig类，EventConfig类读取了使用注解的地方
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(EventConfig.class);

        DemoPublisher publish = context.getBean(DemoPublisher.class);
        publish.publish("你好");
        context.close();
    }
}
