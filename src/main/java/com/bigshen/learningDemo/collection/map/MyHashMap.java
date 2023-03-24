package com.bigshen.learningDemo.collection.map;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author byj
 * @date 2023/1/12
 */

public class MyHashMap {
    @Test
    public void computeIfAbsentTest() {
        Map<String, List<Integer>> map = new HashMap<>(Maps.newHashMapWithExpectedSize(8));
        List<Integer> init = new ArrayList<>();
        init.add(5);
        map.put("test", init);
        // 如果没有找到，则使用指定的函数计算一个默认值
        map.computeIfAbsent("a", k -> new ArrayList<>()).add(1);
        map.computeIfAbsent("b", k -> new ArrayList<>());
        // 使用指定的键查找对应的值，如果找到了，则直接在当前集合中新增；
        map.computeIfAbsent("test", k -> new ArrayList<>()).add(10);
        System.out.println(map);
        for (Map.Entry<String, List<Integer>> stringListEntry : map.entrySet()) {
            System.out.println(stringListEntry.getKey());
            System.out.println(stringListEntry.getValue());
            System.out.println("--------------------------------");
        }
    }
}
