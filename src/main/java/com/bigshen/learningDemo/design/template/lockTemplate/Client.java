package com.bigshen.learningDemo.design.template.lockTemplate;

/**
 * @Author BYJ
 * @Date 2022/5/6 14:54
 * @Describe
 */
public class Client {
    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            new Thread(()->{
                new OrderService().getOrderNumber();
            },String.valueOf(i)).start();
        }

    }
}
