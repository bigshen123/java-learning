package com.bigshen.learningDemo.demo.proxy;

import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.cglib.proxy.Proxy;

import java.lang.reflect.Method;

/**
 * @ClassName ProxyFactory2
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/25
 * @Version V1.0
 **/
public class ProxyFactory2 {
    //维护⽬标对象
    private static Object target;
    //维护关键点代码的类
    private static AOPTest aop;
    public static Object getProxyInstance(Object target_, AOPTest aop_) {
//⽬标对象和关键点代码的类都是通过外界传递进来
        target = target_;
        aop = aop_;
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[]
                            args) throws Throwable {
                        aop.begin();
                        Object returnValue = method.invoke(target, args);
                        aop.close();
                        return returnValue;
                    }
                }
        );
    }
}
