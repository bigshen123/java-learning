package com.bigshen.learningDemo.demo.cache.caffeine;

/**
 * @author byj
 * @date 2023/12/8
 * @Description
 */
public class CaffeineCacheLocalDemo {
    public static void main(String[] args) {
        CaffeineCacheLocal caffeineCacheLocal = new CaffeineCacheLocal();
        caffeineCacheLocal.put("test",1);
        Object o = caffeineCacheLocal.get("test");
        System.out.println(o);
    }
}
