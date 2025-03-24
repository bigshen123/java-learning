package com.bigshen.learningDemo.design.proxy.jdkProxy.serialization;

import java.io.ByteArrayInputStream;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 */
public class HessianInput {
    private final ByteArrayInputStream byteArrayInputStream;

    public HessianInput(ByteArrayInputStream byteArrayInputStream) {
        this.byteArrayInputStream = byteArrayInputStream;
    }

    public Object readObject(Class clazz) {
        return null;
    }
}
