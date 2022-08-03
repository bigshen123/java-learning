package com.bigshen.learningDemo.design.singlton;

/**
 * @ClassName Singlton03
 * @Description:TODO 静态内部类 结合了懒汉、饿汉的优点，真正需要对象时再加载，加载类是线程安全的
 * @Author: byj
 * @Date: 2020/12/1
 */
public class Singlton03 {

    private Singlton03() {
        System.out.println("私有Demo3构造参数初始化");
    }

    public static class SingltonClassInstance{
        private static final Singlton03 singlton03=new Singlton03();
    }

    public static Singlton03 getInstance(){
        return SingltonClassInstance.singlton03;
    }

    public static void main(String[] args) {
        Singlton03 instance1 = Singlton03.getInstance();
        Singlton03 instance2 = Singlton03.getInstance();
        System.out.println(instance1==instance2);
    }

}
