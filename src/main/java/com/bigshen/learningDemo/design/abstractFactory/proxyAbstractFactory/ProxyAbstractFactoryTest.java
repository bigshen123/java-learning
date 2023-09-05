package com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory;

import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.JDKProxy;
import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.impl.EGMCacheAdapter;
import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.impl.IIRCacheAdapter;
import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.impl.CacheServiceImpl;

import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2022/10/9
 *
 * 这里的抽象工厂的创建和获取方式，会采用代理类的方式进行实现。
 * 所被代理的类就是目前的Redis操作方法类，让这个类在不需要任何修改下，就可以实现调用集群A和集群B的数据服务。
 *
 * 并且这里还有一点非常重要，由于集群A和集群B在部分方法提供上是不同的，因此需要做一个接口适配，而这个适配类就相当于工厂中的工厂，
 * 用于创建把不同的服务抽象为统一的接口做相同的业务。
 */
public class ProxyAbstractFactoryTest {
    public static void main(String[] args) {
        CacheService proxyEGM = JDKProxy.getProxy(CacheServiceImpl.class, new EGMCacheAdapter());
        proxyEGM.set("user_name_01", "test1");
        proxyEGM.set("test1","bigshen",10, TimeUnit.MILLISECONDS);
        String val01 = proxyEGM.get("user_name_01");
        System.out.println("测试结果：" + val01);
        String test1 = proxyEGM.get("test1");
        System.out.println("测试结果2：" + test1);

        System.out.println();

        CacheService proxyIIR = JDKProxy.getProxy(CacheServiceImpl.class, new IIRCacheAdapter());
        proxyIIR.set("user_name_01", "test2");
        String val02 = proxyIIR.get("user_name_01");
        System.out.println("测试结果：" + val02);
    }
}
