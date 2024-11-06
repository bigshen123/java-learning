package com.bigshen.springbootDemo.encrypt;

import org.springframework.stereotype.Component;

/**
 * @author gaof
 * @date 2024/10/16
 */
@Component
public class NoOpEncryptStrategy implements EncryptStrategy {

    @Override
    public String getNameKey() {
        return "";
    }

    @Override
    public String getPwdKey() {
        return "";
    }

    @Override
    public String encrypt(String originalValue, String ignore) {
        return originalValue;
    }

    @Override
    public Boolean support(int type) {
        return type == 0;
    }
}
