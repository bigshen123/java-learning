package com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.impl;

import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.ICacheAdapter;
import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.matter.IIR;

import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2022/10/9
 */
public class IIRCacheAdapter implements ICacheAdapter {

    IIR iir = new IIR();

    @Override
    public String get(String key) {
        return iir.get(key);
    }

    @Override
    public void set(String key, String value) {
        iir.set(key, value);
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        iir.setExpire(key, value, timeout, timeUnit);
    }

    @Override
    public void del(String key) {
        iir.del(key);
    }
}
