package com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.impl;

import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.ICacheAdapter;
import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.factory.matter.EGM;

import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2022/10/9
 */
public class EGMCacheAdapter implements ICacheAdapter {

    private final EGM egm = new EGM();

    @Override
    public String get(String key) {
        return egm.gain(key);
    }

    @Override
    public void set(String key, String value) {
        egm.set(key, value);
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        egm.setEx(key, value, timeout, timeUnit);
    }

    @Override
    public void del(String key) {
        egm.delete(key);
    }
}
