package com.bigshen.learningDemo.design.strategy.sort;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author BYJ
 * @Date 2022/8/14 20:37
 * @Describe
 */
public class SortAlgFactory {
    private static final Map<String,ISortAlg> ALGS = new HashMap<>();

    static {
        ALGS.put("QuickSort", new QuickSort());
        ALGS.put("ExternalSort", new ExternalSort());
        ALGS.put("ConcurrentExternalSort", new ConcurrentExternalSort());
        ALGS.put("MapReduceSort", new MapReduceSort());
    }

    public static ISortAlg getSortAlg(String type) {
        if (type == null || type.isEmpty()) {
            throw new IllegalArgumentException("type should not be empty.");
        }
        return ALGS.get(type);
    }
}
