package com.bigshen.learningDemo.design.proxy.jdkProxy.sms;

import com.bigshen.learningDemo.design.proxy.ISender;
import com.bigshen.learningDemo.design.proxy.SmsSender;

import java.lang.reflect.Proxy;

/**
 * @author byj
 * @date 2025/3/21
 * @Description 动态生成一个代理类，并返回一个实现了被代理类接口的代理对象
 * 入参是：类加载器、被代理类的类型、封装了一个被代理对象的InvocationHandler对象
 *
 * JDK动态代理是通过Proxy.newProxyInstance()方法来动态生成代理对象的，
 * JDK动态代理的底层是通过Java反射机制实现的，并且需要目标对象(被代理对象)继承自一个接口才能生成它的代理类。
 */
public class TestJdkProxy {
    public static void main(String[] args) {
        ISender sender = (ISender) Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                new Class[]{ISender.class},
                new JdkProxyHandler(new SmsSender())
        );

        //向代理对象调用被代理类的接口方法
        boolean result = sender.send();
        System.out.println("代理对象：" + sender.getClass().getName());
        System.out.println("输出结果：" + result);
    }
}
