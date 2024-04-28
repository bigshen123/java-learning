package com.bigshen.learningDemo.javaSE.proxy;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @ClassName ProxyFactory
 * @Description: TODO  CGLIB动态代理
 * @Author BYJ
 * @Date 2020/6/25
 * @Version V1.0
 **/
public class ProxyFactory implements MethodInterceptor {

    /**
     维护目标对象
     */
    private Object target;
    public ProxyFactory(Object target){
        this.target=target;
    }

    /**
     * 给目标对象创建代理对象
     * @return
     */
    public Object getProxyInstance(){
        //1、工具类
        Enhancer en=new Enhancer();
        //2、设置父类
        en.setSuperclass(target.getClass());
        //3、设置回调函数
        en.setCallback(this);
        //4、创建子类（代理对象）
        return en.create();
    }


    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("开始事务.......");
        methodProxy.invokeSuper(obj,args);
        // 执⾏⽬标对象的⽅法
        Object returnValue = method.invoke(target, args);
        System.out.println("提交事务.......");
        return returnValue;
    }
    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        UserDao factory = (UserDao) new
                ProxyFactory(userDao).getProxyInstance();
        factory.save();
    }
}

