package com.bigshen.learningDemo.cache.demo;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2025/1/17
 * @Description
 */
public class CacheUtil {

    // 存缓存数据
    private final static Map<String, CacheEntity> CACHE_MAP = new ConcurrentHashMap<>();

    // 定时器线程池，用于清理过期缓存
    private static ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    static {
        // 注册一个定时任务，服务启动 1000 毫秒后，每隔 500 毫秒执行一次
        Runnable task = CacheUtil::clear;
        executorService.scheduleAtFixedRate(task, 1000L, 500L, TimeUnit.MILLISECONDS);
    }

    // 添加缓存
    public static void put(String key, Object value) {
        put(key, value, 0L);
    }

    // 添加缓存
    public static void put(String key, Object value, Long expire) {
        CacheEntity cacheEntity = new CacheEntity();
        cacheEntity.setKey(key);
        cacheEntity.setValue(value);
        if (expire > 0) {
            // 计算过期时间
            Long expireTime = System.currentTimeMillis() + Duration.ofSeconds(expire).toMillis();
            cacheEntity.setExpireTime(expireTime);
        }
        CACHE_MAP.put(key, cacheEntity);
    }

    // 获取
    public static Object get(String key) {
        if (CACHE_MAP.containsKey(key)) {
            return CACHE_MAP.get(key);
        }
        return null;
    }

    // 删除
    public static void remove(String key) {
        CACHE_MAP.remove(key);
    }

    // 清除过期缓存
    public static void clear() {
        if (CACHE_MAP.isEmpty()) {
            return;
        }
        CACHE_MAP.entrySet().removeIf(entityEntry -> entityEntry.getValue().getExpireTime() != null && entityEntry.getValue().getExpireTime() > System.currentTimeMillis());
    }
}

