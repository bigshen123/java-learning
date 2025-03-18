package com.bigshen.learningDemo.jvm.classLoader;

/**
 * @ClassName NoInitialization
 * @Description: TODO  通过子类引用父类的静态字段，不会导致子类初始化
 * @Author BYJ
 * @Date 2020/6/26
 * @Version V1.0
 **/
public class NoInitialization {
    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }
}
