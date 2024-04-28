package com.bigshen.learningDemo.javaSE.genericity;

import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Array;

/**
 * @Author BYJ
 * @Date 2024/2/3 13:38
 * @Describe
 */
public class GenericsDemo30 {
    public static void main(String[] args) {
        Integer i[] = fun1(1, 2, 3, 4, 5, 6);
        fun2(i);
    }

    private static <T> T[] fun1(T... args) {
        return args;
    }

    public static <T> void fun2(T param[]) {
        System.out.println("接受泛型数组...");
        for (T t : param) {
            System.out.println(t + "、");
        }
    }
}
