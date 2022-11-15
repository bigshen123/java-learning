package com.bigshen.learningDemo.demo.lambda.Currying;

/**
 * @ClassName Curry3Args
 * @Description:TODO 我们可以通过添加级别来柯里化一个三参数函数：
 * @Author: byj
 * @Date: 2020/8/4
 */

import java.util.function.Function;

public class Curry3Args {
    public static void main(String[] args) {
        Function<String,
                Function<String,
                        Function<String, String>>> sum =
                a -> b -> c -> a + b + c;
        Function<String,
                Function<String, String>> hi =
                sum.apply("Hi ");
        Function<String, String> ho =
                hi.apply("Ho ");
        System.out.println(ho.apply("Hup"));
    }
}