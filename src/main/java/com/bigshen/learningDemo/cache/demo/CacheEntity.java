package com.bigshen.learningDemo.cache.demo;

import lombok.Data;

/**
 * @author byj
 * @date 2025/1/17
 * @Description
 */
@Data
public class CacheEntity {
    // 缓存键
    private String key;
    // 缓存键
    private Object value;
    // 过期时间
    private Long expireTime;
}

