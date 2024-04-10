package com.bigshen.learningDemo.collection;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ReflectUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author byj
 * @date 2024/4/9
 * @Description 集合增强工具类
 */
public class CollectionPlusUtil {

    public static abstract class Filter<T> {
        /**
         * 判断过滤
         */
        public abstract boolean doFilter(T item);
    }

    /**
     * 集合过滤
     * @return
     */
    public static <T> void filterList(List<T> list, Filter<T> filter){
        Iterator<T> iterator = list.iterator();
        while (iterator.hasNext()){
            T item = iterator.next();
            boolean isFilter = true;
            if(filter != null){
                isFilter = filter.doFilter(item);
            }

            if(isFilter){
                iterator.remove();
            }
        }
    }

    public static <T> Map<String,T> toMap(String key, List<T> list) {
        Map<String,T> map = new HashMap<>();
        list.forEach(item -> {
            map.put((String) ReflectUtil.getFieldValue(item,key),item);
        });
        return map;
    }

    public static <T> Map<String,List<T>> toMapGroup(String groupKey, List<T> list) {
        Map<String,List<T>> map = new HashMap<>();
        list.forEach(item -> {
            String key = (String)ReflectUtil.getFieldValue(item,groupKey);
            List<T> li = map.get(key);
            if(li == null){
                li = CollectionUtil.newArrayList();
                map.put(key,li);
            }
            li.add(item);
        });
        return map;
    }

    public static <T,E> Map<String,List<E>> toMapGroup(String groupKey,String propKey,List<T> list) {
        Map<String,List<E>> map = new HashMap<>();
        list.forEach(item -> {
            String key = (String)ReflectUtil.getFieldValue(item,groupKey);
            List<E> li = map.get(key);
            if(li == null){
                li = CollectionUtil.newArrayList();
                map.put(key,li);
            }
            li.add((E) ReflectUtil.getFieldValue(item,propKey));
        });
        return map;
    }

    public static <T> Map<T,T> toMap(List<T> list) {
        Map<T,T> map = new HashMap<>();
        list.forEach(item -> {
            map.put(item,item);
        });
        return map;
    }

    /**
     * 比较List, 左边有右边没有的集合数据，left属性集合
     * @return
     */
    public static <T> List<T> getDifferenceListProp(List<?> sourceList,String sourcePropName,List<?> targetList,String targetPropName){
        List<?> compareLeftList = sourceList;
        List<?> compareLeftRight = targetList;
        if(sourcePropName != null){
            compareLeftList = getListPropValue(sourceList,sourcePropName);
        }
        if(targetPropName != null){
            compareLeftRight = getListPropValue(targetList,targetPropName);
        }

        List<T> result = CollectionUtil.newArrayList();
        for(Object item : compareLeftList){
            if(!compareLeftRight.contains(item)){
                result.add((T)item);
            }
        }

        return result;
    }

    /**
     * 比较List, 左边有右边没有的集合数据,返回left原始集合
     * @return
     */
    public static <T> List<T> getDifferenceList(List<T> sourceList,String sourcePropName,List<?> targetList,String targetPropName){
        List<?> compareLeftList = sourceList;
        List<?> compareLeftRight = targetList;
        if(sourcePropName != null){
            compareLeftList = getListPropValue(sourceList,sourcePropName);
        }
        if(targetPropName != null){
            compareLeftRight = getListPropValue(targetList,targetPropName);
        }

        List<T> result = CollectionUtil.newArrayList();
        int index = 0;
        for(Object item : compareLeftList){
            if(!compareLeftRight.contains(item)){
                result.add(sourceList.get(index));
            }
            index++;
        }

        return result;
    }

    /**
     * 获取集合中的属性值
     * @return
     */
    public static <R> List<R> getListPropValue(List<?> list,String propName){
        return getListPropValue(list,propName,null);
    }

    /**
     * 获取集合中的属性值
     * @return
     */
    public static <R,T> List<R> getListPropValue(List<?> list,String propName,Filter<T> filter){
        List<R> valueList = CollectionUtil.newArrayList();
        if(list == null){
            return valueList;
        }
        for(Object object : list){
            boolean isFilter = true;
            if(filter != null){
                isFilter = filter.doFilter((T)object);
            }

            if(isFilter){
                Object value = ReflectUtil.getFieldValue(object,propName);
                if(value instanceof Collection){
                    valueList.addAll((Collection<? extends R>) value);
                }else{
                    valueList.add((R)value);
                }
            }
        }
        return valueList;
    }

    /**
     * 求差集
     * @param <T>
     * @return
     */
    public static <T> List<T> getAllDifferenceList(List<?> list1, List<?> list2){
        List<T> resultList = new ArrayList<>();
        List<T> resultList1 = (List<T>) list1.stream().filter(t-> !list2.contains(t)).collect(Collectors.toList());
        List<T> resultList2 = (List<T>) list2.stream().filter(t-> !list1.contains(t)).collect(Collectors.toList());
        resultList.addAll(resultList1);
        resultList.addAll(resultList2);
        return resultList;
    }

    /**
     * 求差集
     * @param <T>
     * @return
     */
    public static <T> List<T> getDifferenceList(List<?> list1, List<?> list2){
        //List<T> resultList = (List<T>)list1.stream().filter(t-> !list2.contains(t)).collect(Collectors.toList());
        //return resultList;

        List<T> resultList = (List<T>) list1.stream().filter(t-> !list2.contains(t)).collect(Collectors.toList());
        //resultList.stream().forEach(System.out::println);
        return resultList;
    }

    /**
     * 求交集
     * @param <T>
     * @return
     */
    public static <T> List<T>  getIntersectionList(List<?> list1,List<?> list2){
        List<T> resultList = (List<T>)list1.stream().filter(t-> list2.contains(t)).collect(Collectors.toList());
        return resultList;
    }

    /**
     * 求并集
     * @param <T>
     * @return
     */
    public static <T> List<T>  getAllList(List<?> list1,List<?> list2){
        List<T> resultList = new ArrayList<>();
        resultList.addAll((List<T>)list1);
        resultList.addAll((List<T>)list2);
        return resultList;
    }
}
