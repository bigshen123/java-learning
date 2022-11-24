package com.bigshen.learningDemo.leetcode.HashMap;

import com.bigshen.learningDemo.utils.json.JacksonUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author byj
 * @date 2022/10/28
 * @deprecated 对比两个map去除相同kv，返回主map的v
 */
public class CheckKeyValueByMap {
    public static void main(String[] args) {
        String oldJson = "{\"localStoreLog\":{\"retentionDays\":100,\"currentRetentionDays\":0,\"maxUsePercent\":90,\"usedPercent\":0,\"partName\":\"/dev/sda2\"},\"forwardLog\":{\"generalLogType\":[\"SystemServices\",\"HotStandby\",\"Kern\",\"Services\"],\"telegrafLogType\":[],\"addresses\":[{\"forwardIp\":\"10.0.210.152\",\"forwardPort\":\"514\"}]},\"satelliteConfig\":{\"enable\":false,\"interName\":\"\",\"systemId\":\"\",\"gwSrvTypeIdentify\":\"10070\",\"tcpAddr\":\"\",\"udpAddr\":\"\"}}\n";
        String newJson = "{\"localStoreLog\":{\"retentionDays\":100,\"currentRetentionDays\":0,\"maxUsePercent\":90,\"usedPercent\":0,\"partName\":\"/dev/sda2\"},\"forwardLog\":{\"generalLogType\":[\"SystemServices\",\"HotStandby\",\"Kern\",\"Services\"],\"telegrafLogType\":[],\"addresses\":[{\"forwardIp\":\"10.0.210.152\",\"forwardPort\":\"514\"}]},\"satelliteConfig\":{\"enable\":false,\"interName\":\"\",\"systemId\":\"\",\"gwSrvTypeIdentify\":\"10070\",\"tcpAddr\":\"\",\"udpAddr\":\"\"}}\n";
        LinkedHashMap<String,Object> oldMap = JacksonUtil.parseObject(oldJson,LinkedHashMap.class,String.class,Object.class);
        LinkedHashMap<String,Object> newMap = JacksonUtil.parseObject(newJson,LinkedHashMap.class,String.class,Object.class);
        LinkedHashMap<String, Object> updateMap = checkAndDiscardSameValueKeyByMap(oldMap, newMap);
        String updateJson = JacksonUtil.toJsonString(updateMap);
        if (updateMap.isEmpty()){
            System.out.println("配置完全一致");
        }
        Map<String,Object> oldMap2 = JacksonUtil.parseObject(oldJson,Map.class,String.class,Object.class);
        Map<String,Object> newMap2 = JacksonUtil.parseObject(newJson,Map.class,String.class,Object.class);
        System.out.println(oldMap2.equals(newMap2));
        System.out.println(oldMap2.toString().equals(newMap2.toString()));
        System.out.println(updateJson);
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atomicInteger.incrementAndGet();
        System.out.println(atomicInteger.get());
    }

    /**
     * 对比两个map去除相同kv，返回主map的kv
     * @param localConfig
     * @param newConfig
     * @return
     */
    public static LinkedHashMap<String, Object> checkAndDiscardSameValueKeyByMap(@NotNull LinkedHashMap<String, Object> localConfig,
                                                                                 @NotNull LinkedHashMap<String, Object> newConfig) {
        LinkedHashMap<String, Object> newMap = new LinkedHashMap<>(newConfig.size());
        newConfig.forEach((key, value) -> {
            Object localObject = localConfig.get(key);
            if (localObject == null) {
                newMap.put(key, value);
            } else if (value instanceof Map) {
                if (localObject instanceof Map) {
                    LinkedHashMap<String, Object> newValue = checkAndDiscardSameValueKeyByMap((LinkedHashMap<String, Object>) localObject, (LinkedHashMap<String, Object>) value);
                    if (!newValue.isEmpty()) {
                        newMap.put(key, newValue);
                    }
                } else if (!((LinkedHashMap<String, Object>) value).isEmpty()) {
                    newMap.put(key, value);
                }
            } else if (value instanceof Collection) {
                if (localObject instanceof Collection) {
                    if (checkCollectionValueIsDiff((Collection<Object>) localObject, (Collection<Object>) value)) {
                        newMap.put(key, value);
                    }
                } else if (!((Collection<Object>) value).isEmpty()) {
                    newMap.put(key, value);
                }
            } else if (!value.equals(localObject)) {
                newMap.put(key, value);
            }
        });
        return newMap;
    }
    /**
     * 对比集合值是否一致(去除""值以及 空集合 空map 进行排序比较，包含所有子集)
     *
     * @param oldCollection 老集合
     * @param newCollection 新集合
     * @return true 表示 新老集合内容不一样(含子孙)
     */
    public static boolean checkCollectionValueIsDiff(@NotNull Collection<Object> oldCollection, @NotNull Collection<Object> newCollection) {
        Collection<Object> oldOrderCollection = toOrderAndDiscardEmptyCollection(oldCollection);
        Collection<Object> newOrderCollection = toOrderAndDiscardEmptyCollection(newCollection);
        String oldConfigMd5 = DigestUtils.md5Hex(JacksonUtil.toJsonString(oldOrderCollection));
        String newConfigMd5 = DigestUtils.md5Hex(JacksonUtil.toJsonString(newOrderCollection));
        return !oldConfigMd5.equals(newConfigMd5);
    }
    /**
     * 将Collection进行排序 子Map 或子Collection isEmpty 为true  则 丢弃（值为空串也丢弃），返回新的排序Collection
     *
     * @param oldCollection 老Map
     * @return 新Collection （TreeSet）
     */
    private static Collection<Object> toOrderAndDiscardEmptyCollection(@NotNull Collection<Object> oldCollection) {
        TreeSet<Object> newList = new TreeSet<>(Comparator.comparing(JacksonUtil::toJsonString));
        oldCollection.forEach(v -> {
            if (v instanceof Map) {
                if (!((Map<String, Object>) v).isEmpty()) {
                    newList.add(toOrderAndDiscardEmptyMap((Map<String, Object>) v));
                }
            } else if (v instanceof Collection) {
                if (!((Collection<Object>) v).isEmpty()) {
                    newList.add(toOrderAndDiscardEmptyCollection((Collection<Object>) v));
                }
            } else if (v != null && !"".equals(v)) {
                newList.add(v);
            }
        });
        return newList;
    }
    /**
     * 将Map 进行排序 子Map 或子Collection isEmpty 为true  则 丢弃（值为空串也丢弃），返回新的排序Map
     *
     * @param oldMap 老Map
     * @return 新Map (TreeMap)
     */
    @SuppressWarnings("unchecked")
    public static <V> Map<String, Object> toOrderAndDiscardEmptyMap(@NotNull Map<String, V> oldMap) {
        TreeMap<String, Object> newMap = new TreeMap<>();
        oldMap.forEach((k, v) -> {
            if (v instanceof Map) {
                if (!((Map<String, Object>) v).isEmpty()) {
                    newMap.put(k, toOrderAndDiscardEmptyMap((Map<String, V>) v));
                }
            } else if (v instanceof Collection) {
                if (!((Collection<Object>) v).isEmpty()) {
                    newMap.put(k, toOrderAndDiscardEmptyCollection((Collection<Object>) v));
                }
            } else if (v != null && !"".equals(v)) {
                newMap.put(k, v);
            }
        });
        return newMap;
    }
}
