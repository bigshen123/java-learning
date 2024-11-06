package com.bigshen.springbootDemo.encrypt;

import java.util.List;

/**
 * @author byj
 * @date 2024/10/22
 * @Description 策略模式 demo
 */
public class ExternalApiAuthenticationHandler {

    private final List<? extends EncryptStrategy> encryptStrategies;

    public ExternalApiAuthenticationHandler(List<? extends EncryptStrategy> encryptStrategies){
        this.encryptStrategies = encryptStrategies;
        this.authentication();
    }

    public void authentication() {
        EncryptStrategy nameEncryptStrategy = encryptStrategies.stream().filter(i -> i.support(2)).findFirst()
                .orElseThrow(() -> new RuntimeException("用户名字段的加密类型为2,未实现该加密方式"));
        System.out.println(nameEncryptStrategy.getNameKey());
        System.out.println(nameEncryptStrategy.getPwdKey());

    }

}
