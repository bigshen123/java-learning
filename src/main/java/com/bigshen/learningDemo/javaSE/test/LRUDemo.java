package com.bigshen.learningDemo.javaSE.test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description:redis的LRU算法实现
 * @Author: BIGSHEN
 * @Date: 2019/12/9 17:33
 */
public class LRUDemo<K, V> extends LinkedHashMap<K, V> {
    private final int CACHE_SIZE;
    /**
     传递进来最多可以缓存多少数据
     */
    public LRUDemo(int cacheSize) {
        // 这块就是设置一个hashmap的初始大小，同时最后一个true指的是让linkedhashmap按照访问顺序来进行排序，
        // 最近访问的放在头，最老访问的就在尾
        super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
        this.CACHE_SIZE = cacheSize;
    }
    @Override
    protected boolean removeEldestEntry(Map.Entry entry) {
        // 这个意思就是说当map中的数据量大于指定的缓存个数的时候，就自动删除最老的数据
        return size() > CACHE_SIZE;
    }

}
