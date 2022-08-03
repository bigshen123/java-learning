package com.bigshen.learningDemo.demo.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName UserService
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/25
 * @Version V1.0
 **/
@Component
public class UserService {
    private UserDao userDao ;
    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}
