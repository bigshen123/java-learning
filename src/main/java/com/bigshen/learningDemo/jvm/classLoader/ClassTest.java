package com.bigshen.learningDemo.jvm.classLoader;

/**
 * @Author BYJ
 * @Date 2022/5/1 9:49
 * @Describe
 */
public class ClassTest {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        //获取class对象的三种方法
        System.out.println("根据类名： \t" + ClassTest.class);
        System.out.println("根据对象: \t" + new ClassTest().getClass());
        System.out.println("根据全限定类名：\t" + Class.forName("com.bigshen.learningDemo.demo.jvm.classLoader.ClassTest"));

        Class<ClassTest> classTestClass = ClassTest.class;
        System.out.println(classTestClass.getClass());
        System.out.println(classTestClass.getSimpleName());
        System.out.println(classTestClass.getModifiers());
        System.out.println(classTestClass.getSuperclass());
        System.out.println(classTestClass.getInterfaces());
        System.out.println(classTestClass.newInstance());
    }
}
