package com.bigshen.learningDemo.jvm.classLoading;

/**
 * @ClassName NoInitialization02
 * @Description: TODO  通过数组定义来引用类，不会触发此类的初始化
 * @Author BYJ
 * @Date 2020/6/26
 * @Version V1.0
 **/
public class NoInitialization02 {
    public static void main(String[] args) {
        SuperClass[] sca=new SuperClass[10];
    }
}
