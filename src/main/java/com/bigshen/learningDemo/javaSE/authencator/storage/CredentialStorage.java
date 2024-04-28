package com.bigshen.learningDemo.javaSE.authencator.storage;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 18:46
 */
public interface CredentialStorage {
    String getPasswordByAppID(String appId);
}
