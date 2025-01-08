package com.bigshen.learningDemo.jvm.classLoading;

/**
 * @ClassName SubClass
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/26
 * @Version V1.0
 **/
public class SubClass extends com.bigshen.learningDemo.jvm.classLoading.SuperClass {
    static {
        System.out.println("SubClass init!!!");
    }
}
