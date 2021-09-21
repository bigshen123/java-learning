package com.bigshen.chatDemoService.leetcode.HashMap;

/**
 * @Author BYJ
 * @Date 2021/3/14 20:35
 * @Describe
 */
public class TestMyHashMap {
    public static void main(String[] args) {
        MyHashMap map=new MyHashMap();
        map.put(1,2);
        System.out.println(map.get(1));
        map.put(1,3);
        System.out.println(map.get(1));
        map.remove(1);
        System.out.println(map.get(1));
    }
}
