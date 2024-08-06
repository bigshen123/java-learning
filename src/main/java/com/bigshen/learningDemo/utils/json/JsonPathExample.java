package com.bigshen.learningDemo.utils.json;

import com.jayway.jsonpath.*;

/**
 * @author byj
 * @date 2024/7/16
 * @Description
 */
public class JsonPathExample {
    public static void main(String[] args) {
        // Sample JSON string
        String jsonString = "{ \"store\": { \"book\": [ { \"category\": \"reference\", \"author\": \"Nigel Rees\", \"title\": \"Sayings of the Century\", \"price\": 8.95 }, { \"category\": \"fiction\", \"author\": \"Evelyn Waugh\", \"title\": \"Sword of Honour\", \"price\": 12.99 } ], \"bicycle\": { \"color\": \"red\", \"price\": 19.95 } } }";

        // Configure JsonPath 以便在路径不存在时返回 null 并抑制异常。
        Configuration jsonPathConfiguration = Configuration.builder()
                .options(Option.DEFAULT_PATH_LEAF_TO_NULL, Option.SUPPRESS_EXCEPTIONS)
                .build();
        ParseContext parseContext = JsonPath.using(jsonPathConfiguration);

        // Parse JSON string
        DocumentContext documentContext = parseContext.parse(jsonString);

        // Read specific key values
        String authorOfFirstBook = documentContext.read("$.store.book[0].author");
        String colorOfBicycle = documentContext.read("$.store.bicycle.color");

        // Print the values
        System.out.println("Author of the first book: " + authorOfFirstBook);
        System.out.println("Color of the bicycle: " + colorOfBicycle);
    }
}
