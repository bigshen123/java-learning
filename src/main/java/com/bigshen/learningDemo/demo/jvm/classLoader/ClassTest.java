package com.bigshen.learningDemo.demo.jvm.classLoader;

/**
 * @Author BYJ
 * @Date 2022/5/1 9:49
 * @Describe
 */
public class ClassTest {
    public static void main(String[] args) throws ClassNotFoundException {
        //获取class对象的三种方法
        System.out.println("根据类名： \t" + ClassTest.class);
        System.out.println("根据对象: \t" + new ClassTest().getClass());
        System.out.println("根据全限定类名：\t" + Class.forName("com.bigshen.learningDemo.demo.jvm.classLoader.ClassTest"));
    }
}
