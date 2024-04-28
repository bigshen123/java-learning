package com.bigshen.learningDemo.javaSE.reflections;

import com.bigshen.learningDemo.common.annotation.Path;
import org.junit.Test;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author byj
 * @date 2022/10/18
 * Reflections库提供了一种方便的方式来扫描类路径，查找和使用注解、注释和特定类型的子类。这对于编写通用代码、插件化和扩展化应用程序等方案非常有用。
 * Reflections通过扫描classpath，索引元数据，并且允许在运行时查询这些元数据。
 * <p>
 * 使用Reflections可以很轻松的获取以下元数据信息：
 * <p>
 * 1）获取某个类型的所有子类；比如，有一个父类是TestInterface，可以获取到TestInterface的所有子类。
 * <p>
 * 2）获取某个注解的所有类型/字段变量，支持注解参数匹配。
 * <p>
 * 3）使用正则表达式获取所有匹配的资源文件
 * <p>
 * 4）获取特定签名方法。
 */
public class ReflectionsDemo {


    @Test
    public void reflectionsDemo1() {
        Class<?> reflectionsDemo = null;
        try {
            reflectionsDemo = Class.forName("com.bigshen.learningDemo.javaSE.reflections.ReflectionsDemo");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println(reflectionsDemo);
        Class<ReflectionsDemo> reflectionsDemoClass = ReflectionsDemo.class;
        System.out.println(reflectionsDemoClass);
        ReflectionsDemo r = new ReflectionsDemo();
        Class<? extends ReflectionsDemo> aClass = r.getClass();
        System.out.println(aClass);
    }

    @Test
    public void reflectionsDemo2() {
        //创建Reflections对象，并指定要扫描的包路径：
        Reflections reflections = new Reflections("com.bigshen.learningDemo");
        // 查找指定类：
        Set<Class<? extends ArrayList>> subTypesOf = reflections.getSubTypesOf(ArrayList.class);
        // 查找注解：
        Set<Class<?>> annotatedClasses = reflections.getTypesAnnotatedWith(Path.class);

        /*Reflections reflections = new Reflections(ReflectionsDemo.class.getPackage().getName());
        Set<Class<? extends RedisService>> subTypesOf = reflections.getSubTypesOf(RedisService.class);
        System.out.println(subTypesOf);
        System.out.println(System.currentTimeMillis());*/



        /*// 初始化工具类
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages).addScanners(new SubTypesScanner()).addScanners(new FieldAnnotationsScanner()));

// 获取某个包下类型注解对应的类
        Set<Class<?>> typeClass = reflections.getTypesAnnotatedWith(RpcInterface.class, true);

// 获取子类
        Set<Class<? extends SomeType>> subTypes = reflections.getSubTypesOf(SomeType.class);

// 获取注解对应的方法
        Set<Method> resources =reflections.getMethodsAnnotatedWith(SomeAnnotation.class);

// 获取注解对应的字段
        Set<Field> ids = reflections.getFieldsAnnotatedWith(javax.persistence.Id.class);

// 获取特定参数对应的方法
        Set<Method> someMethods = reflections.getMethodsMatchParams(long.class, int.class);

        Set<Method> voidMethods = reflections.getMethodsReturn(void.class);

        Set<Method> pathParamMethods =reflections.getMethodsWithAnyParamAnnotated(PathParam.class);
// 获取资源文件
        Set<String> properties = reflections.getResources(Pattern.compile(".*\\.properties"));*/
    }

}
