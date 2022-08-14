/*
 * @Copyright: 2018-2019 taojiji All rights reserved.
 */
package com.bigshen.learningDemo.utils.copy;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
public class CommonUtil {

	/**
	 * 获取contextId
	 * 
	 * @return
	 */
	public static String getContextId() {
		return System.currentTimeMillis() + UUID.randomUUID().toString();
	}

	/**
	 * 简单属性copy
	 * 
	 * @param s
	 * @param clazz
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> T convertBean(S s, Class<T> clazz) {
		if (s == null) {
			return null;
		}
		try {
			T t = clazz.newInstance();
			BeanUtils.copyProperties(s, t);
			return t;
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("拷贝属性异常", e);
			throw new RuntimeException("拷贝属性异常");
		}
	}

	/**
	 * list进行元素拷贝
	 * 
	 * @param sources
	 * @param clazz
	 * @param <S>
	 * @param <T>
	 * @return
	 */
	public static <S, T> List<T> convertBeanList(List<S> sources, Class<T> clazz) {
		return sources.stream().map(source -> convertBean(source, clazz)).collect(Collectors.toList());
	}

	/**
	 * request请求参数转换为map
	 * 
	 * @param object
	 * @return
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> requestToMap(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		Field[] superclassFields = FieldUtils.getAllFields(object.getClass());
		List<Field> fieldList = new ArrayList<>();
		fieldList.addAll(Arrays.asList(fields));
		fieldList.addAll(Arrays.asList(superclassFields));
		Map<String, Object> params = new HashMap<>();
		try {
			for (Field field : fieldList) {
				field.setAccessible(true);
				if (field.isAnnotationPresent(RequireField.class)) {
					if (field.getType() == String.class) {
						params.put(field.getName(),
								StringUtils.isNotBlank(String.valueOf(field.get(object))) ? field.get(object) : null);
					} else if (field.getType() == Integer.class) {
						params.put(field.getName(), field.get(object) != null ? field.get(object) : null);
					} else if (field.getType() == Date.class) {
						params.put(field.getName(), field.get(object) != null ? field.get(object) : null);
					}
				}
			}
		} catch (IllegalAccessException e) {
			log.error("参数转化异常...", e);
			throw new RuntimeException("参数转化异常...", e);
		}
		return params;
	}
}
