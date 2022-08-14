package com.bigshen.learningDemo.demo.authencator.storage;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 18:48
 */
public class MySqlCredentialStorage implements CredentialStorage {
    @Override
    public String getPasswordByAppID(String appId) {
        return "123";
    }
}
