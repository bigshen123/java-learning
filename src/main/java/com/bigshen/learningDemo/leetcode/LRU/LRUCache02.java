package com.bigshen.learningDemo.leetcode.LRU;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName LRUCache02
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/5/26
 * @Version V1.0
 **/
public class LRUCache02 {
    int capacity;
    LinkedHashMap<Integer,Integer> cache;

    public LRUCache02(int capacity){
        this.capacity=capacity;
        cache=new LinkedHashMap<Integer, Integer>(capacity,0.75f,true){
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest){
                return cache.size()>capacity;
            }
        };
    }

    public int get(int key){
        return cache.getOrDefault(key,-1);
    }
    public void put(int key,int value){
        cache.put(key,value);
    }
}
