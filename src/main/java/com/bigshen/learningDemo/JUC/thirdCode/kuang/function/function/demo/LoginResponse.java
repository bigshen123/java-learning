package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.function.demo;

import lombok.Getter;

/**
 * @author byj
 * @date 2024/12/18
 * @Description
 */
@Getter
public class LoginResponse {

    private final String message;

    public LoginResponse(String message) {
        this.message = message;
    }

}
