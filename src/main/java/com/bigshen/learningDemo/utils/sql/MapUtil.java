package com.bigshen.learningDemo.utils.sql;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.*;
import java.util.Map.Entry;

public class MapUtil {
	
	/**
	 * 将Map的key转小写
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> listMapKeyToUpperCase(List<Map<String, Object>> list) {
		if (CollectionUtils.isEmpty(list)) {
			return list;
		}
		
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		for (Map<String, Object> map : list) {
			Map<String, Object> resultMap = mapKeyToUpperCase(map);
			resultList.add(resultMap);
		}
		return resultList;
	}
	
	/**
	 * 将Map的key转小写
	 * @param map
	 * @return
	 */
	public static Map<String, Object> mapKeyToUpperCase(Map<String, Object> map) {
		if (MapUtils.isEmpty(map)) {
			return map;
		}
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if (map.isEmpty()) {
			return map;
		}
		
		Set<Entry<String,Object>> entrySet = map.entrySet();
		for (Entry<String,Object> entry : entrySet) {
			String key = entry.getKey().toUpperCase();
			resultMap.put(key, entry.getValue());
		}
		return resultMap;
	}

}
