package com.bigshen.learningDemo.design.proxy;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 */
public class SmsSender implements ISender {
    public boolean send() {
        System.out.println("Sending msg");
        return true;
    }
}