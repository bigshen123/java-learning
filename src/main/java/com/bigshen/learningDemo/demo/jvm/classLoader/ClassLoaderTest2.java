package com.bigshen.learningDemo.demo.jvm.classLoader;

/**
 * @ClassName ClassLoaderTest2
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/10/21
 * @Version V1.0
 **/
public class ClassLoaderTest2{
    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);
        System.out.println(classLoader.getParent());
        System.out.println(classLoader.getParent().getParent());
    }
}
