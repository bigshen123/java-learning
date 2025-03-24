package com.bigshen.learningDemo.design.proxy.jdkProxy.sms;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author byj
 * @date 2025/3/21
 * @Description //第三步：定义一个InvocationHandler类
 */
public class JdkProxyHandler implements InvocationHandler {
    private Object target;

    public JdkProxyHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("处理前");
        Object result = method.invoke(target, args);
        System.out.println("处理后");
        return result;
    }
}
