package com.bigshen.learningDemo.jvm.classLoading;

/**
 * @ClassName SuperClass
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/26
 * @Version V1.0
 **/
public class SuperClass {
    static {
        System.out.println("SuperClass init!!!");
    }
    public static int value=123;
}
