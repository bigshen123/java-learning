package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.consumer;

/**
 * @author byj
 * @date 2024/11/25
 * @Description
 */
public class ConsumerAndThenTest {
    public static void main(String[] args) {
        test2(msg1 -> {
            System.out.println(msg1 + "-> 转换为小写：" + msg1.toLowerCase());
        }, msg2 -> {
            System.out.println(msg2 + "-> 转换为大写：" + msg2.toUpperCase());
        }, msg3 -> {
            System.out.println(msg3 + "-> 值不变:" + msg3);
        });
    }

    public static void test2(Consumer<String> c1, Consumer<String> after, Consumer<String> before) {
        String str = "Hello World";
        after.andThen(c1, before).accept(str);
    }
}

