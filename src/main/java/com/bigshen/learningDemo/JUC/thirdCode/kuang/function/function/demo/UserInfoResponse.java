package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.function.demo;

import lombok.Getter;

/**
 * @author byj
 * @date 2024/12/18
 * @Description
 */
@Getter
public class UserInfoResponse {

    private final String username;
    private final String email;

    public UserInfoResponse(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
