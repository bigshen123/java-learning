package com.bigshen.learningDemo.design.singlton;

/**
 * @ClassName Singlton04
 * @Description:TODO 枚举单例式：实现简单，枚举本来就是单例。缺点：没有延迟加载
 * @Author: byj
 * @Date: 2020/12/1
 * Effective Java 作者推荐使用枚举的方式解决单例模式，此种方式可能是平时最少用到的。
 * 这种方式解决了最主要的；线程安全、自由串行化、单一实例。
 */
public class SingltonEnum {

    public static SingltonEnum getInstance(){
        return Demo.INSTANCE.getInstance();
    }
    //定义枚举
    private enum Demo{
        INSTANCE;
        //枚举元素为单例
        private SingltonEnum singlton04;
        Demo(){
            System.out.println("枚举Demo私有构造参数");
            singlton04=new SingltonEnum();
        }
        public SingltonEnum getInstance(){
            return singlton04;
        }
    }

    public static void main(String[] args) {
        SingltonEnum instance1 = SingltonEnum.getInstance();
        SingltonEnum instance2 = SingltonEnum.getInstance();
        System.out.println(instance1==instance2);
    }

}
