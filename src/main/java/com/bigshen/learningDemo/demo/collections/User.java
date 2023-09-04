package com.bigshen.learningDemo.demo.collections;

import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author BYJ
 * @Date 2022/12/27 16:41
 * @Describe
 */
@ToString
@EqualsAndHashCode(callSuper = false)
public class User {

    private String userId;
    private String userName;
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
