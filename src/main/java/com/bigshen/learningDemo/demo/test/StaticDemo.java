package com.bigshen.learningDemo.demo.test;

/**
 * @Author BYJ
 * @Date 2023/8/27 9:35
 * @Describe
 */
public class StaticDemo {

    public static void main(String[] args) {

    }

    public static void staticMethod() {
        StaticDemo staticDemo = new StaticDemo();
        staticDemo.instanceMethod();
        // 直接调用非静态方法：编译报错
        // instanceMethod();
    }
    public void instanceMethod() {
        System.out.println("非静态方法");
    }
}
