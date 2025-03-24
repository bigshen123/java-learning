package com.bigshen.learningDemo.design.proxy.staticProxy;

import com.bigshen.learningDemo.design.proxy.ISender;
import com.bigshen.learningDemo.design.proxy.SmsSender;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 * 静态代理：
 * 如果要对一个实现某接口的类的一个方法进行增强，在不影响原接口的前提下只能重新实现该接口。
 * 如果要增强的类有很多，那么每一个类都需要重新实现一遍，比较麻烦。
 * 比如在下面的例子中，如果还要代理IReceiver接口的实现类，那么还需要定义一个ProxyReceiver代理类去实现IReceiver接口。
 * 因为具体的代理类是需要实现被代理类的接口的。
 */
public class TestStaticProxy {
    public static void main(String[] args) {
        ISender sender = new ProxySender(new SmsSender());
        boolean result = sender.send();
        System.out.println("输出结果：" + result);
    }
}
