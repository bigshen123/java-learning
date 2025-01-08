package com.bigshen.learningDemo.jvm.classLoading;

/**
 * @ClassName NoInitialization03
 * @Description: TODO  常量在编译阶段会存在调用类的常量池中，本质上内有直接引用到定义常量的类，因此不会触发定义常量的类初始化
 * @Author BYJ
 * @Date 2020/6/26
 * @Version V1.0
 **/
public class NoInitialization03 {
    public static void main(String[] args) {
        System.out.println(ConstClass.HELLOWORLD);
    }
}
