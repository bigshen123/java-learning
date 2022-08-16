package com.bigshen.learningDemo.demo.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @ClassName ListDemo
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/15
 */
public class ListDemo {

    public static void main(String[] args) {
        List<String> parentList = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            parentList.add(String.valueOf(i));
        }
        List<String> subList = parentList.subList(1, 3);

        // 1. 修改子list值
        subList.set(0, "new1");

        for (String s : parentList) {
            System.out.println(s);
        }
        // output :0 new1 2 3 4

        // 2.修改子list结构
        subList.add(String.valueOf(2.5));

        // 3.修改父list值
        parentList.set(2, "new2");
        // output: new1,new2,2.5

        if(subList.size()>0){
            Iterator<String> iterator = subList.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                System.out.println(next);
            }
        }

        // output:java.util.ConcurrentModificationException

    }
}
