package com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory;


import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2022/10/9
 */
public interface CacheService {
    String get(final String key);

    void set(String key, String value);

    void set(String key, String value, long timeout, TimeUnit timeUnit);

    void del(String key);
}
