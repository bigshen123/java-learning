package com.bigshen.learningDemo.utils;

import com.bigshen.learningDemo.utils.json.JacksonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;

/**
 * @author byj
 * @date 2023/3/24
 */
public class JsonDiffUtil {


    /**
     * 对比两个json并返回差异json
     *
     * @param json1 json1
     * @param json2 json2
     * @return diffJson
     */
    public static String compareAndReturnDiff(String json1, String json2) {
        ObjectMapper objectMapper = new ObjectMapper();
        String diffJson = "";
        try {
            JsonNode tree1 = objectMapper.readTree(json1);
            JsonNode tree2 = objectMapper.readTree(json2);
            ObjectNode diff = objectMapper.createObjectNode();
            compare(tree1, tree2, diff, "");
            diffJson = JacksonUtil.toJsonString(diff);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return diffJson;
    }

    /**
     * 对比两个JsonNode
     *
     * @param node1  node1
     * @param node2  node2
     * @param diff   差异node
     * @param prefix 前缀
     */
    private static void compare(JsonNode node1, JsonNode node2, ObjectNode diff, String prefix) {
        if (node1.isObject() && node2.isObject()) {
            Iterator<String> fieldNames = node1.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (node2.has(fieldName)) {
                    compare(node1.get(fieldName), node2.get(fieldName), diff, prefix + fieldName + ".");
                } else {
                    diff.set(prefix + fieldName, node1.get(fieldName));
                }
            }
            fieldNames = node2.fieldNames();
            while (fieldNames.hasNext()) {
                String fieldName = fieldNames.next();
                if (!node1.has(fieldName)) {
                    diff.set(prefix + fieldName, node2.get(fieldName));
                }
            }
        } else if (node1.isArray() && node2.isArray()) {
            for (int i = 0; i < node1.size() || i < node2.size(); i++) {
                if (i < node1.size() && i < node2.size()) {
                    compare(node1.get(i), node2.get(i), diff, prefix + "[" + i + "].");
                } else if (i < node1.size()) {
                    diff.set(prefix + "[" + i + "]", node1.get(i));
                } else if (i < node2.size()) {
                    diff.set(prefix + "[" + i + "]", node2.get(i));
                }
            }
        } else if (!node1.equals(node2)) {
            diff.set(prefix.substring(0, prefix.length() - 1), node2);
        }
    }

    public static void main(String[] args) {
        String json1 = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        String json2 = "{\"name\": \"John\", \"age\": 31, \"city\": \"Los Angeles\"}";

        System.out.println("JSON 1: " + json1);
        System.out.println("JSON 2: " + json2);
        System.out.println("Difference: " + compareAndReturnDiff(json1, json2));
    }
}
