package com.bigshen.learningDemo.demo.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.github.benmanes.caffeine.cache.stats.CacheStats;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2023/12/8
 * @Description 本地Caffeine封装
 */
public class CaffeineCacheLocal {

    private static final CaffeineCacheLocal CACHE;

    static {
        // 50年
        long defaultWriteInMillis = 1576800000000L;
        CACHE = CaffeineCacheLocal
                .newBuilder()
                .maximumSize(10000)
                .build();
        //.expireAfterWrite(defaultWriteInMillis,TimeUnit.MILLISECONDS);
    }

    private static CaffeineCacheLocal getCache(){
        return CACHE;
    }

    private final Cache<String, Object> localCache;

    private RemovalListener removalListener;
    private long maximumSize = Integer.MAX_VALUE;
    private long duration = -1L;
    private TimeUnit unit;


    public CaffeineCacheLocal() {
        localCache = initCache();
    }

    public CaffeineCacheLocal(RemovalListener removalListener, long maximumSize, long duration, TimeUnit unit) {
        if (removalListener != null) {
            this.removalListener = removalListener;
        }
        if (unit != null) {
            this.unit = unit;
        }
        this.duration = duration;
        this.maximumSize = maximumSize;
        this.localCache = initCache();
    }

    public static Builder<Object, Object> newBuilder() {
        return new Builder<>();
    }

    private static class Builder<K1, V1> {
        private RemovalListener removalListener;
        private long maximumSize;
        private long duration;
        private TimeUnit unit;

        public Builder<K1, V1> removalListener(RemovalListener removalListener) {
            this.removalListener = removalListener;
            return this;
        }

        public Builder<K1, V1> maximumSize(long maximumSize) {
            this.maximumSize = maximumSize;
            return this;
        }

        public Builder<K1, V1> expireAfterWrite(long duration, TimeUnit unit) {
            this.duration = duration;
            this.unit = unit;
            return this;
        }

        public CaffeineCacheLocal build() {
            return new CaffeineCacheLocal(removalListener, maximumSize, duration, unit);
        }
    }

    private Cache<String, Object> initCache() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
        //暂时未加入权重逻辑 所以maximumSize必须设定
        //若加入权重逻辑后,可以根据是否有权重判断处理
        if (this.maximumSize <= 0L) {
            throw new RuntimeException("maximumSize is must be set");
        }

        // key的最大size
        caffeine.maximumSize(this.maximumSize);

        // expireAfterWrite全局时间淘汰策略
        if (this.duration > 0L && this.unit != null) {
            caffeine.expireAfterWrite(this.duration, this.unit);
        }

        // 开启淘汰监听
        if (this.removalListener != null) {
            caffeine.removalListener(this.removalListener);
        }

        return caffeine.build();
    }

    public void put(String key, final Object value) {
        localCache.put(key, value);
    }

    public <T> T get(String key) {
        if (Objects.nonNull(key)){
            return (T)localCache.getIfPresent(key);
        }
        return null;
    }

    public void remove(String key) {
        localCache.invalidate(key);
    }

    /**
     * 批量删除 指定Key前缀的缓存
     *
     * @param keyPrefix
     */
    public void removeAll(String keyPrefix) {
        localCache.asMap().keySet().iterator().forEachRemaining(key -> {
                    if (key.startsWith(keyPrefix)) {
                        localCache.invalidate(key);
                    }
                }
        );
    }

    public CacheStats cacheStats() {
        return localCache.stats();
    }

    public void clear() {
        localCache.invalidateAll();
    }

    public long size() {
        return localCache.estimatedSize();
    }

    public void cleanUp() {
        localCache.cleanUp();
    }

    public <T> T getOrDefault(String k, Object defaultValue) {
        Object v;
        return (T) (((v = get(k)) != null) ? v : defaultValue);
    }

    public Map all() {
        return localCache.asMap();
    }

}
