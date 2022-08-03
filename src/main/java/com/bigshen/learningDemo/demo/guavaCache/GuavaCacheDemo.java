package com.bigshen.learningDemo.demo.guavaCache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 本地缓存 guavaCache
 * @Author: BIGSHEN
 * @Date: 2019/12/30 16:58
 */
public class GuavaCacheDemo {
    public static void main(String[] args) throws Exception {
        LoadingCache<Integer, String> localCache = CacheBuilder.newBuilder()
                // 设置并发级别为8，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                // 最大容量
                .maximumSize(200000)
                // 初始容量
                .initialCapacity(100000)
                // 写入后5秒过期
                .expireAfterWrite(5, TimeUnit.SECONDS)
                // 统计缓存的命中率
                .recordStats()
                // 设置缓存移除通知
                .removalListener((RemovalListener<Integer, String>) notification ->
                        System.out.println(notification.getKey() + " was removed,cause is "
                        + notification.getCause())).build(new CacheLoader<Integer, String>() {

                    // 在缓存不存在时，通过CacheLoader自动加载缓存
                    @Override
                    public String load(Integer key)  {
                        System.out.println("load data:" + key);
                        return key + "对应的 value";
                    }

                });

        // ------------

        for (int i = 1; i <= 10; i++) {
            System.out.println("key:" + i + ",value:" + localCache.get(i));
            System.out.println("key:" + i + ",value:" + localCache.get(i));
            // 休眠2s
            Thread.sleep(2000);

            // 打印缓存中的命中情况
            System.out.println("cache status:");
            System.out.println(localCache.stats().toString());
        }

    }
}
