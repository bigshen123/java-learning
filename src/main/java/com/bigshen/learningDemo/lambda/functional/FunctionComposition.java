package com.bigshen.learningDemo.demo.lambda.functional;

/**
 * @ClassName FunctionComposition
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/8/4
 */

import java.util.function.Function;

public class FunctionComposition {
    static Function<String, String>
            f1 = s -> {
        System.out.println(s);
        return s.replace('A', '_');
    },
            f2 = s -> s.substring(3),
            f3 = s -> s.toLowerCase(),
            f4 = f1.compose(f2).andThen(f3);
    public static void main(String[] args) {
        System.out.println(
                f4.apply("GO AFTER ALL AMBULANCES"));
    }
}
