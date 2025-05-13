package com.bigshen.learningDemo.jvm.classLoader.breakClassLoad;

/**
 * @author byj
 * @date 2025/4/10
 * @Description 我们会将这个 .class 文件手动编译，并通过自定义 ClassLoader 加载，从而绕过默认的父类加载机制。
 */
public class Hello {
    public void say() {
        System.out.println("Hello from custom class loader!");
    }
}
