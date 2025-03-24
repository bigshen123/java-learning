package com.bigshen.learningDemo.design.proxy.cglibProxy.bd;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 */
public class CglibProxyInterceptor implements MethodInterceptor {
    private Enhancer enhancer = new Enhancer();

    //获取代理类
    //@param clazz 被代理类
    public Object getProxy(Class clazz) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("处理前");
        Object result = methodProxy.invokeSuper(object,args);
        System.out.println("处理后");
        return result;
    }
}
