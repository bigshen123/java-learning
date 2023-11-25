package com.bigshen.learningDemo.jni.sdfDemo.exception;

/**
 * @Author BYJ
 * @Date 2023/11/16 22:17
 * @Describe Jni层返回的异常信息
 */
public class JniNativeException extends Exception {

    public JniNativeException(String message) {
        super(message);
    }

}
