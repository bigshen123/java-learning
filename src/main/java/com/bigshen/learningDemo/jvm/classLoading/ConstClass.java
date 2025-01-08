package com.bigshen.learningDemo.jvm.classLoading;

/**
 * @ClassName ConstClass
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/26
 * @Version V1.0
 **/
public class ConstClass {
    static {
        System.out.println("ConstClass init!!!");
    }
    public static final String HELLOWORLD="hello world";
}
