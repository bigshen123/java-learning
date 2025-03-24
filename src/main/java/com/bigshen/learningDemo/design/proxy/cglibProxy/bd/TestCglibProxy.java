package com.bigshen.learningDemo.design.proxy.cglibProxy.bd;

/**
 * @author byj
 * @date 2025/3/21
 * @Description 和JDK动态代理不同，CGLib动态代理不需要目标对象实现自一个接口，只需要实现一个处理代理逻辑的切入类以及实现MethodInterceptor接口。
 * <p>
 * CGLib动态代理的特点如下：
 * 使用CGLib实现动态代理，完全不受被代理类必须实现自一个接口的限制。
 * CGLib底层采用ASM字节码生成框架，使用字节码技术生成代理类比使用Java反射的效率要高。
 * CGLib不能对声明为final的方法进行代理，因为CGLib原理是动态生成被代理类的子类。
 */
public class TestCglibProxy {
    public static void main(String[] args) {
        BdSender sender = (BdSender) new CglibProxyInterceptor().getProxy(BdSender.class);
        boolean result = sender.send();
        System.out.println("代理对象：" + sender.getClass().getName());
        System.out.println("输出结果：" + result);
    }
}
