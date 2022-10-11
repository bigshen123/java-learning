package com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.impl;

import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.CacheService;
import com.bigshen.learningDemo.design.abstractFactory.proxyAbstractFactory.util.RedisUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2022/10/9
 */
public class CacheServiceImpl implements CacheService {
    private RedisUtils redisUtils = new RedisUtils();

    @Override
    public String get(String key) {
        return redisUtils.get(key);
    }

    @Override
    public void set(String key, String value) {
        redisUtils.set(key, value);
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        redisUtils.set(key, value, timeout, timeUnit);
    }

    @Override
    public void del(String key) {
        redisUtils.del(key);
    }
}
