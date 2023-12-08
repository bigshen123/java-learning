package com.bigshen.learningDemo.demo.cache.guavaCache;

import com.google.common.cache.*;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2023/12/5
 * @Description
 */
public class GuavaDemo {
    public static void main(String[] args) throws Exception {
        //LoadingCache<String, Object> cache = createCacheLoader();
        //CacheLoader的方式创建
        LoadingCache<String,Object> cache= CacheBuilder.newBuilder().build(new CacheLoader<String, Object>() {

            //读取数据源
            @Override
            public Object load(String key) {
                return Constants.hm.get(key);
            }
        });
        /*
        初始化cache
        */
        initCache(cache);
        System.out.println(cache.size());
        displayCache(cache);
        System.out.println("=============================");
        Thread.sleep(1000);
        System.out.println(cache.getIfPresent("1"));
        Thread.sleep(2500);
        System.out.println("=============================");
        displayCache(cache);
        deleteCache();
    }

    private static void deleteCache() throws Exception {
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                //最大个数
                .maximumSize(3)
                .build(new CacheLoader<String, Object>() {
                    //读取数据源
                    @Override
                    public Object load(String key) {
                        return Constants.hm.get(key);
                    }
                });
        //读取缓存中的1的数据 缓存有就读取 没有就返回null
        System.out.println(cache.getIfPresent("5"));
        //读取4 读源并回写缓存 淘汰一个（LRU+FIFO）
        System.out.println(get("4", cache));

        // 主动删除
        //cache.invalidate("1");
        cache.invalidateAll();
        System.out.println("1111111111111111111111111111");
        displayCache(cache);
        // cache.invalidateAll(Arrays.asList("1","2"));


    }

    private static LoadingCache<String, Object> createCacheLoader() {
        return CacheBuilder.newBuilder()
                // 最大3个 //Cache中存储的对象,写入3秒后过期
                .maximumSize(3)
                .expireAfterWrite(3, TimeUnit.SECONDS)
                //记录命中率 失效通知
                .recordStats()
                .removalListener(new RemovalListener<Object, Object>() {
                    @Override
                    public void onRemoval(RemovalNotification<Object, Object> notification) {
                        System.out.println(notification.getKey() + ":" + notification.getCause());
                    }
                }).build(new CacheLoader<Object, Object>() {
                    @Override
                    public Object load(Object key) {
                        return Constants.hm.get(key);
                    }
                });
    }

    /**
     * 读取缓存数据 如果没有则回调源数据并(自动)写入缓存
     * @param key
     * @param cache
     * @return
     * @throws Exception
     */
    public static Object get(String key, LoadingCache<String,Object> cache) throws Exception {
        Object value = cache.get(key, (Callable) () -> {
            Object v = Constants.hm.get(key);
            //设置回缓存
            cache.put(key, v);
            return v;
        });
        return value;
    }

    /**
     * 前三条记录
     */
    public static void initCache(LoadingCache<String,Object> cache) throws Exception {
        for (int i = 1; i <= 3; i++) {
            //连接数据源   如果缓存没有则读取数据源
            cache.get(String.valueOf(i));
        }
    }

    /**
     * 获得当前缓存的记录
     *
     * @param cache
     * @throws Exception
     **/
    public static void displayCache(LoadingCache<String,Object> cache) throws Exception {
        for (Map.Entry<String, Object> stringObjectEntry : cache.asMap().entrySet()) {
            System.out.println(stringObjectEntry.toString());
        }
    }
}


