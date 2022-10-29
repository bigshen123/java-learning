package com.bigshen.learningDemo.utils.json;

import com.jayway.jsonpath.JsonPath;

/**
 * @author byj
 * @date 2022/10/25
 */
public class JsonPathUtil {
    public static void main(String[] args) {
        String json = "{\"store\":{\"book\":[{\"category\":\"reference\",\"author\":\"Nigel Rees\",\"title\":\"Sayings of the Century\",\"price\":8.95},{\"category\":\"fiction\",\"author\":\"Evelyn Waugh\",\"title\":\"Sword of Honour\",\"price\":12.99},{\"category\":\"fiction\",\"author\":\"Herman Melville\",\"title\":\"Moby Dick\",\"isbn\":\"0-553-21311-3\",\"price\":8.99},{\"category\":\"fiction\",\"author\":\"J. R. R. Tolkien\",\"title\":\"The Lord of the Rings\",\"isbn\":\"0-395-19395-8\",\"price\":22.99}],\"bicycle\":{\"color\":\"red\",\"price\":19.95}}}\n";
        System.out.println("获取book的author节点:" + JsonPath.read(json, "$.store.book[*].author"));
        System.out.println("所有author节点：" + JsonPath.read(json, "$..author"));
        System.out.println("匹配第三个book节点：" + JsonPath.read(json, "$..book[2]"));
        System.out.println("匹配倒数第一个book节点：" + JsonPath.read(json, "$..book[-1:]"));
        System.out.println("过滤含isbn字段的节点：" + JsonPath.read(json, "$..book[?(@.isbn)]"));
        System.out.println("过滤含pice小于10的节点：" + JsonPath.read(json, "$..book[?(@.price<10)]"));
        System.out.println("递归匹配所有子节点：" + JsonPath.read(json, "$..*"));

    }
}
