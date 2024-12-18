package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.function.demo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author byj
 * @date 2024/12/18
 * @Description
 */
@Setter
@Getter
public class LoginRequest {

    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

}
