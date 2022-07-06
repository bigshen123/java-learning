package com.bigshen.chatDemoService.demo;

import com.bigshen.chatDemoService.concurrent.kuang.stream.User;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author byj
 * @date 2022/2/17
 */
public class GetObjectByReflectionDemo {
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> clazz = Class.forName("com.bigshen.chatDemoService.concurrent.kuang.stream.User");
        User user = (User) clazz.newInstance();
        Constructor<?> constructor  = clazz.getDeclaredConstructor(String.class, int.class, int.class);
        User user1 = (User) constructor.newInstance("bigshen", 10, 18);

    }
}
