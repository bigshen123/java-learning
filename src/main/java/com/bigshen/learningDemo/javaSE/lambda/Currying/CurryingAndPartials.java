package com.bigshen.learningDemo.javaSE.lambda.Currying;

/**
 * @ClassName CurryingAndPartials
 * @Description:TODO 柯里化（Currying）的名称来自于其发明者之一 Haskell Curry。
 * 他可能是计算机领域唯一姓氏和名字都命名过重要概念的人（另外就是 Haskell 编程语言）。 柯里化意为：将一个多参数的函数，转换为一系列单参数函数。
 * @Author: byj
 * @Date: 2020/8/4
 */

import java.util.function.Function;

public class CurryingAndPartials {
    // 未柯里化:
    static String uncurried(String a, String b) {
        return a + b;
    }
    public static void main(String[] args) {
        // 柯里化的函数:
        Function<String, Function<String, String>> sum =
                a -> b -> a + b; // [1]

        System.out.println(uncurried("Hi ", "Ho"));

        Function<String, String>
                hi = sum.apply("Hi "); // [2]
        System.out.println(hi.apply("Ho"));

        // 部分应用:
        Function<String, String> sumHi =
                sum.apply("Hup ");
        System.out.println(sumHi.apply("Ho"));
        System.out.println(sumHi.apply("Hey"));
    }
}