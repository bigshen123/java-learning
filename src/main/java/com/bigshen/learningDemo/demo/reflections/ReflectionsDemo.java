package com.bigshen.learningDemo.demo.reflections;

import com.bigshen.learningDemo.common.service.RedisService;
import org.apache.commons.configuration.ConfigurationBuilder;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;

import javax.websocket.server.PathParam;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author byj
 * @date 2022/10/18
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
    public static void main(String[] args) {
        Reflections reflections = new Reflections(ReflectionsDemo.class.getPackage().getName());
        Set<Class<? extends RedisService>> subTypesOf = reflections.getSubTypesOf(RedisService.class);
        System.out.println(subTypesOf);
        System.out.println(System.currentTimeMillis());

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
