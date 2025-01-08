package com.bigshen.learningDemo.jvm.reflections;

import com.bigshen.learningDemo.javaSE.collections.map.hashmap.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * @Author BYJ
 * @Date 2022/11/6 20:31
 * @Describe
 */
@Slf4j
public class ReflectionsTest {

    @Test
    public void classTest() throws Exception {
        // 获取Class对象的三种方式
        log.info("根据类名:  \t" + User.class);
        log.info("根据全限定类名:\t" + Class.forName("com.bigshen.learningDemo.javaSE.collections.map.hashmap.User"));
        // 常用的方法
        log.info("获取全限定类名:\t" + new User().getName());
        log.info("实例化:\t" + User.class.newInstance());

        Class<?> c1 = Class.forName("com.bigshen.learningDemo.javaSE.collections.map.hashmap.User");
        Constructor<?> conn = c1.getConstructor(String.class, int.class);
        User bigshen = (User)conn.newInstance("bigshen", 26);
        Field name = c1.getDeclaredField("name");
        name.set(bigshen,"zhangsan");
        System.out.println(bigshen);

        ArrayList<Integer> array = new ArrayList<>();
        Class<? extends ArrayList> c = array.getClass();
        Method m = c.getMethod("add", Object.class);
        m.invoke(array,"hello");
        m.invoke(array,"world");
        m.invoke(array,"java");
        System.out.println(array);

    }
}
