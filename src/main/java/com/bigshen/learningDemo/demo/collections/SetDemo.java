package com.bigshen.learningDemo.demo.collections;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author BYJ
 * @Date 2022/12/27 16:42
 * @Describe
 */
public class SetDemo {
    public static void main(String[] args) {
        Set<User> users = new HashSet<>();
        User user = new User();
        for (int i = 0; i < 5; i++) {
            users.add(user);
        }
        user.setUserId("1");
        user.setUserName("test");
        user.setEmail("123");
        System.out.println(users);
    }
}
