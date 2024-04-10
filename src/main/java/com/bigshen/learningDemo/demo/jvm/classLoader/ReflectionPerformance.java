package com.bigshen.learningDemo.demo.jvm.classLoader;

import com.bigshen.learningDemo.collection.map.hashmap.User;
import org.junit.Test;

import java.lang.reflect.Method;

/**
 * @Author BYJ
 * @Date 2022/5/6 18:44
 * @Describe 反射性能测试
 */
public class ReflectionPerformance {

    /**
     * 普通方法
     */
    @Test
    public void test01(){
        User user = new User();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
            user.getName();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("普通方式执行10亿次getName的时间:" + (endTime - startTime) + " ms");
    }

    /**
     * 反射方式调用
     */
    @Test
    public void test02() throws Exception {
        Class<?> clazz = Class.forName("com.bigshen.learningDemo.collection.map.hashmap.User");
        Method getName = clazz.getDeclaredMethod("getName", (Class<?>) null);
        User user = (User) clazz.newInstance();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
            getName.invoke(user, null);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("反射方式执行10亿次getName的时间:" + (endTime - startTime) + " ms");
    }

    /**
     * 反射方式调用，关闭权限检查
     */
    @Test
    public void test03() throws Exception {
        Class<?> clazz = Class.forName("com.moxi.interview.study.annotation.User");
        Method getName = clazz.getDeclaredMethod("getName", (Class<?>) null);
        User user = (User) clazz.newInstance();
        long startTime = System.currentTimeMillis();
        getName.setAccessible(true);
        for (int i = 0; i < 1000000000; i++) {
            getName.invoke(user, null);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("反射方式执行10亿次getName的时间:" + (endTime - startTime) + " ms");
    }
}
