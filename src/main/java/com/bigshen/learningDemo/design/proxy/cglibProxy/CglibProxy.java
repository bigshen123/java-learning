package com.bigshen.learningDemo.design.proxy.cglibProxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName CglibProxy
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/1
 */
public class CglibProxy implements MethodInterceptor {

    private Object target;
    public Object getInstance(Object target){
        this.target=target;
        Enhancer enhancer=new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("开启事务");
        Object result = methodProxy.invoke(target, args);
        System.out.println("关闭事务");
        return result;
    }

    public static void main(String[] args) {
        CglibProxy cglibProxy=new CglibProxy();
        UserDao cglibProxyInstance= (UserDao) cglibProxy.getInstance(new UserDaoImpl());
        cglibProxyInstance.save();
    }
}
