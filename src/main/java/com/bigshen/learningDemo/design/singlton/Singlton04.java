package com.bigshen.learningDemo.design.singlton;

/**
 * @ClassName Singlton04
 * @Description:TODO 枚举单例式：实现简单，枚举本来就是单例。缺点：没有延迟加载
 * @Author: byj
 * @Date: 2020/12/1
 */
public class Singlton04 {

    public static Singlton04 getInstance(){
        return Demo.INSTANCE.getInstance();
    }
    //定义枚举
    private static enum Demo{
        INSTANCE;
        //枚举元素为单例
        private Singlton04 singlton04;
        private Demo(){
            System.out.println("枚举Demo私有构造参数");
            singlton04=new Singlton04();
        }
        public Singlton04 getInstance(){
            return singlton04;
        }
    }

    public static void main(String[] args) {
        Singlton04 instance1 = Singlton04.getInstance();
        Singlton04 instance2 = Singlton04.getInstance();
        System.out.println(instance1==instance2);
    }

}
