package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.function;

public class FunctionDemo2 {
    public static void main(String[] args) {
        test(msg -> {
            return Integer.parseInt(msg);
        });
    }

    public static void test(Function<String, Integer> function) {
        Integer apply = function.apply("666");
        System.out.println("apply = " + apply);
    }
}

