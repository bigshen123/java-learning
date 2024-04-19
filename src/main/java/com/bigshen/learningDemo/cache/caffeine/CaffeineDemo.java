package com.bigshen.learningDemo.demo.cache.caffeine;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author byj
 * @date 2023/12/8
 * @Description https://blog.csdn.net/crazymakercircle/article/details/113751575
 */
public class CaffeineDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        //demo();

        //manual();

       // loading();

        //asyncLoading();

        //  Caffeine提供三类驱逐eviction策略：基于大小（size-based），基于时间（time-based）和基于引用（reference-based）。


    }

    private static void asyncLoading() throws ExecutionException, InterruptedException {
        AsyncLoadingCache<String, Integer> cache = Caffeine.newBuilder().buildAsync(name -> {
            System.out.println("name:" + name);
            return 18;
        });
        CompletableFuture<Integer> ageFuture = cache.get("张三");

        Integer age = ageFuture.get();
        System.out.println("age:" + age);
    }

    private static void manual() {
        Cache<String, Integer> cache = Caffeine.newBuilder().build();

        Integer age1 = cache.getIfPresent("张三");
        System.out.println(age1);

        //当key不存在时，会立即创建出对象来返回，age2不会为空
        Integer age2 = cache.get("张三", k -> {
            System.out.println("k:" + k);
            return 18;
        });
        System.out.println(age2);
    }

    private static void demo() {
        Cache<String, String> cache = Caffeine.newBuilder()
                //5秒没有读写自动删除
                .expireAfterAccess(3, TimeUnit.SECONDS)
                //最大容量1024个，超过会自动清理空间
                .maximumSize(1024)
                .removalListener(((key, value, cause) -> {
                    System.out.println("回收键："+key+"值："+value+"原因："+cause);
                    //清理通知 key,value ==> 键值对   cause ==> 清理原因
                }))
                .build();

        //添加值
        cache.put("张三", "浙江");
        //获取值
        System.out.println(cache.getIfPresent("张三"));

        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(cache.getIfPresent("张三"));

        //remove
//        cache.invalidate("张三");
//        String cacheData2 = cache.getIfPresent("张三");
//        System.out.println(cacheData2);
    }

    private static void loading() {
        //此时的类型是 LoadingCache 不是 Cache
        LoadingCache<String, Integer> cache = Caffeine.newBuilder().build(key -> {
            System.out.println("自动填充:" + key);
            return 18;
        });

        Integer age1 = cache.getIfPresent("张三");
        System.out.println(age1);

        // key 不存在时 会根据给定的CacheLoader自动装载进去
        Integer age2 = cache.get("张三");
        System.out.println(age2);
    }
}
