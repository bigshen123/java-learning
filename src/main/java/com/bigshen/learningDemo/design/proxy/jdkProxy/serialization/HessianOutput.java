package com.bigshen.learningDemo.design.proxy.jdkProxy.serialization;

import java.io.ByteArrayOutputStream;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 */
public class HessianOutput {
    private final ByteArrayOutputStream byteArrayOutputStream;

    public HessianOutput(ByteArrayOutputStream byteArrayOutputStream) {
        this.byteArrayOutputStream = byteArrayOutputStream;
    }

    public void writeObject(Object object) {

    }
}
