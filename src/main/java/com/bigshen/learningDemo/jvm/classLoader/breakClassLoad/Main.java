package com.bigshen.learningDemo.jvm.classLoader.breakClassLoad;

/**
 * @author byj
 * @date 2025/4/10
 * @Description
 * javac -d ./hello-classes Hello.java
 * javac -d ./out MyClassLoader.java Main.java
 * java -cp ./out Main
 *
 * 输出：
 * Hello from custom class loader!
 * 类加载器为: MyClassLoader@<hash>
 */
public class Main {
    public static void main(String[] args) throws Exception {
        String classDir = "src/main/java"; // Hello.class 编译输出目录

        MyClassLoader loader = new MyClassLoader(classDir);
        Class<?> clazz = loader.loadClass("com.bigshen.learningDemo.jvm.classLoader.breakClassLoad.Hello");

        Object instance = clazz.getDeclaredConstructor().newInstance();
        clazz.getMethod("say").invoke(instance);

        System.out.println("类加载器为: " + clazz.getClassLoader());
    }
}
