package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.function.demo;

import lombok.Getter;

/**
 * @author byj
 * @date 2024/12/18
 * @Description
 */
@Getter
public class UserInfoRequest {

    private final int userId;

    public UserInfoRequest(int userId) {
        this.userId = userId;
    }

}
