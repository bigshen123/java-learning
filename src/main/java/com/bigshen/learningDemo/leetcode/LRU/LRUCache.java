package com.bigshen.learningDemo.leetcode.LRU;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName LRUCache
 * @Description: TODO
 * 运用你所掌握的数据结构，设计和实现一个  LRU (最近最少使用) 缓存机制。它应该支持以下操作： 获取数据 get 和 写入数据 put 。
 *
 * 获取数据 get(key) - 如果密钥 (key) 存在于缓存中，则获取密钥的值（总是正数），否则返回 -1。
 * 写入数据 put(key, value) - 如果密钥已经存在，则变更其数据值；如果密钥不存在，则插入该组「密钥/数据值」。当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值，
 * 从而为新的数据值留出空间。
 *
 * @Author BYJ
 * @Date 2020/5/26
 * @Version V1.0
 **/
public class LRUCache {
    private int cap;
    /**
     保持插入顺序
     */
    private Map<Integer,Integer> map=new LinkedHashMap<>();
    public LRUCache(int capacity){
        this.cap=capacity;
    }
    public int get(int key){
        if (map.containsKey(key)){
            int value = map.get(key);
            map.remove(key);
            //保证在末尾
            map.put(key,value);
            return value;
        }
        return -1;
    }
    public void put(int key,int value){
        if (map.containsKey(key)){
            map.remove(key);
        }else if (map.size()==cap){
            Iterator<Map.Entry<Integer, Integer>> iterator = map.entrySet().iterator();
            iterator.next();
            iterator.remove();
        }
        map.put(key,value);
    }

    public static void main(String[] args) {
        LRUCache lruCache=new LRUCache(10);
        lruCache.put(1,1);
        lruCache.put(2,2);
        lruCache.put(3,3);
        lruCache.put(4,4);

        lruCache.get(2);
        lruCache.get(3);
        lruCache.put(5,5);
        lruCache.map.entrySet().forEach(System.out::println);

    }

}
