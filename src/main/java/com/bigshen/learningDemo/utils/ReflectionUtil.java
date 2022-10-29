package com.bigshen.learningDemo.utils;

import com.bigshen.learningDemo.common.service.RedisService;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author byj
 * @date 2022/7/13
 * 反射 util
 */
public class ReflectionUtil {

    @Autowired
    RedisService redisService;
    /**
     * 获取spring 代理对象的真实实例
     * @param proxy 代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws Exception {
        if(!AopUtils.isAopProxy(proxy)) {
            //不是代理对象
            return proxy;
        }

        if(AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else { //cglib
            return getCglibProxyTargetObject(proxy);
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);
        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
        return target;
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);
        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);
        Object target = ((AdvisedSupport)advised.get(aopProxy)).getTargetSource().getTarget();
        return target;
    }

    /**
     * 用于单测调用private方法
    * */
    public void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("TestServiceImpl");
        Method method = clazz.getDeclaredMethod("test2");
        method.setAccessible(true);
        Object target = ReflectionUtil.getTarget(redisService);
        // 注意，这里不能直接用serviceImpl，因为它已经被spring管理，
        // 变成serviceImpl真实实例的代理类，而代理类中并没有私有方法，所以需要先获取它的真实实例
        method.invoke(target);
    }
}
