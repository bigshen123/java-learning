package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.predicate;

/**
 * @author byj
 * @date 2024/11/25
 * @Description Predicate 接口，根据定义的泛型参数进行判断，最终返回一个boolean值。
 */
@FunctionalInterface
interface Predicate<T> {
    boolean test(T t);
}
// 使用
public class PredicateDemo {
    public static void main(String[] args) {
        test(msg -> {
            return msg.length() > 3;
        }, "HelloWorld");
    }

    private static void test(Predicate<String> predicate, String msg) {
        boolean b = predicate.test(msg);
        System.out.println("b:" + b);
    }
}
