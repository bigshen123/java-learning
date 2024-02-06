package com.bigshen.learningDemo.lambda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author byj
 * @date 2023/12/18
 * @Description
 */
public class MerfeDemo {

    public static final ConcurrentHashMap<String, ConcurrentHashMap<String, Long>> TENANT_MEM_POINTER_MAP = new ConcurrentHashMap<>();

    public static void main(String[] args) {
//        Map<String, HashMap<String, Integer>> loadMap = new HashMap<>();
//        Demo aa1 = new Demo("11", "aa", 100);
//        Demo aa2 = new Demo("11", "bb", 100);
//        Demo aa3 = new Demo("11", "aa", 1000);
//        Demo aa4 = new Demo("22", "aa", 1000);
//        Demo aa5 = new Demo("22", "aa", 100);
//        demo1(loadMap,aa1);
//        demo1(loadMap,aa2);
//        demo1(loadMap,aa3);
//        demo1(loadMap,aa4);
//        demo1(loadMap,aa5);
//        System.out.println(loadMap);
//        Map<String, HashMap<String, Integer>> loadMap2 = new HashMap<>();
//        demo2(loadMap2,aa1);
//        demo2(loadMap2,aa2);
//        demo2(loadMap2,aa3);
//        demo2(loadMap2,aa4);
//        demo2(loadMap2,aa5);
//        System.out.println(loadMap2);

        putLocalCacheMemory("11","aa",11111L);
        putLocalCacheMemory("11","bb",22222L);
        putLocalCacheMemory("22","cc",33333L);
        putLocalCacheMemory("11","aa",4444L);
        System.out.println(TENANT_MEM_POINTER_MAP);
        removeLocalCacheMemory("11","cc");
        removeLocalCacheMemory("11","bb");
        removeLocalCacheMemory("22","aa");
        removeLocalCacheMemory("22","cc");
        System.out.println(TENANT_MEM_POINTER_MAP);

    }

    private static void demo2(Map<String, HashMap<String, Integer>> loadMap,Demo demo) {
        String tenantId = demo.getTenantId();
        String caId = demo.getCaId();
        Integer maxCrlCount = demo.getMaxCrlCount();
        loadMap.computeIfAbsent(tenantId, k -> new HashMap<>())
                .merge(caId, maxCrlCount, Integer::max);
    }

    private static void demo1(Map<String, HashMap<String, Integer>> loadMap,Demo demo) {
        String tenantId = demo.getTenantId();
        String caId = demo.getCaId();
        Integer maxCrlCount = demo.getMaxCrlCount();
        if (loadMap.containsKey(tenantId)){
            HashMap<String, Integer> caMap = loadMap.get(tenantId);
            if (caMap.containsKey(caId)) {
                if (maxCrlCount > caMap.get(caId)) {
                    // 每个caId下加载最大的count
                    caMap.put(caId, maxCrlCount);
                }
            } else {
                caMap.put(caId, maxCrlCount);
            }
        }else {
            HashMap<String, Integer> caMap = new HashMap<>();
            caMap.put(caId,maxCrlCount);
            loadMap.put(tenantId, caMap);
        }
    }

    private static void removeLocalCacheMemory(String tenantId, String caId) {
        TENANT_MEM_POINTER_MAP.computeIfPresent(tenantId, (k, v) -> {
            v.remove(caId);
            return v.isEmpty() ? null : v;
        });
    }

    private static void putLocalCacheMemory(String tenantId, String caId, Long memPointer) {
        TENANT_MEM_POINTER_MAP.computeIfAbsent(tenantId, k -> new ConcurrentHashMap<>())
                .put(caId, memPointer);
    }

    @Data
    @AllArgsConstructor
    static class Demo{
        String tenantId;
        String caId;
        Integer maxCrlCount;
    }
}
