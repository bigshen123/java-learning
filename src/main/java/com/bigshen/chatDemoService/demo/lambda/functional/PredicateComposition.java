package com.bigshen.chatDemoService.demo.lambda.functional;

/**
 * @ClassName PredicateComposition
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/8/4
 */

import java.util.function.Predicate;
import java.util.stream.Stream;

public class PredicateComposition {
    static Predicate<String>
            p1 = s -> s.contains("bar"),
            p2 = s -> s.length() < 5,
            p3 = s -> s.contains("foo"),
            p4 = p1.negate().and(p2).or(p3);
    public static void main(String[] args) {
        Stream.of("bar", "foobar", "foobaz", "fongopuckey")
                .filter(p4)
                .forEach(System.out::println);
    }
}
