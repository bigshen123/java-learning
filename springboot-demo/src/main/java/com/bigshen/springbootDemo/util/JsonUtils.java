package com.bigshen.springbootDemo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author byj
 * @date 2025/5/29
 * @Description
 */
public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    // 对象转 JSON 字符串
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Json序列化失败: " + e.getMessage(), e);
        }
    }

    // JSON 字符串转对象
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Json反序列化失败: " + e.getMessage(), e);
        }
    }
}
