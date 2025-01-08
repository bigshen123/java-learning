package com.bigshen.learningDemo.javaSE.annotation;

import com.bigshen.learningDemo.jvm.reflections.ReflectionUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Author BYJ
 * @Date 2024/2/3 15:45
 * @Describe
 */
public class Test {
    public static void main(String[] args) {
        try {
            // 获取所有methods
            Method[] methods = ReflectionUtil.getMethods("com.bigshen.learningDemo.javaSE.annotation.TestMethodAnnotation");

            // 遍历
            for (Method method : methods) {
                // 方法上是否有MyMethodAnnotation注解
                if (method.isAnnotationPresent(MyMethodAnnotation.class)) {
                    try {
                        // 获取并遍历方法上的所有注解
                        for (Annotation anno : method.getDeclaredAnnotations()) {
                            System.out.println("Annotation in Method '"
                                    + method + "' : " + anno);
                        }

                        // 获取MyMethodAnnotation对象信息
                        MyMethodAnnotation methodAnno = method
                                .getAnnotation(MyMethodAnnotation.class);

                        System.out.println(methodAnno.title());

                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
