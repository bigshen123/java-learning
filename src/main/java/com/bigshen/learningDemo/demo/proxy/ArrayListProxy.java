package com.bigshen.learningDemo.demo.proxy;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/22 12:06
 */
public class ArrayListProxy {
    public static void main(String[] args) {
        final List<String> list=new ArrayList<String>();
        list.add("我好");
        List<String> proxyInstance = (List<String>) Proxy.newProxyInstance(list.getClass().getClassLoader(),
                list.getClass().getInterfaces(), (proxy, method, args1) -> method.invoke(list, args1));
        proxyInstance.add("你好!");
        list.add("1");
        System.out.println(list);
    }
}
