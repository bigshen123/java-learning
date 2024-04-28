package com.bigshen.learningDemo.javaSE.test;

import java.util.*;

/**
 * @ClassName ListMapNullTest
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/8/31
 * @Version V1.0
 **/
public class ListMapNullTest {
    public static void main(String[] args) {
        List arrayList=new ArrayList();
        arrayList.add(null);
        arrayList.add(null);
        arrayList.add(null);
        System.out.println(arrayList.size());
        List linkList=new LinkedList();
        linkList.add(null);
        linkList.add(null);
        linkList.add(null);
        System.out.println(linkList.size());
        //map只能设置一个null的key
        Map<String,Object> hashMap=new HashMap<>();
        hashMap.put(null,"bigshen");
        hashMap.put(null,null);
        for (Map.Entry<String, Object> entry : hashMap.entrySet()) {
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
        }
    }
}
