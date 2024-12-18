package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.supplier.demo;

import java.util.function.Supplier;

/**
 * @author byj
 * @date 2024/12/18
 * @Description
 */
public class SupplierHandlerConfigDemo {
    public static void main(String[] args) {

        // 创建一个简单的 Supplier，返回一个问候语
        Supplier<String> greetingSupplier = () -> "Hello, World!";

        // 创建一个 Supplier，返回一个数字
        Supplier<Integer> numberSupplier = () -> 42;

        // 创建 SupplierHandlerConfig 配置对象
        SupplierHandlerConfig<String> greetingHandler = new SupplierHandlerConfig<>(greetingSupplier);

        // 创建一个新的 Supplier，它将 greetingSupplier 转换为返回 Integer
        Supplier<Integer> greetingAsNumberSupplier = () -> greetingSupplier.get().length();  // 转换为返回字符串长度

        // 使用链式执行（greetingAsNumberSupplier -> numberSupplier）
        Supplier<String> combinedSupplier = greetingHandler.chainWithAfter(() -> numberSupplier.get().toString());

        // 执行操作，获得结果
        System.out.println("Executing chainWithAfter:");
        String result = combinedSupplier.get();  // 执行链式调用
        System.out.println(result);  // 输出：42


        // 使用 chainWithBefore（转换 greetingSupplier 为 Integer 类型）
        SupplierHandlerConfig<Integer> numberHandler = new SupplierHandlerConfig<>(numberSupplier);
        Supplier<Integer> combinedBefore = numberHandler.chainWithBefore(greetingAsNumberSupplier);

        // 执行操作，获得结果
        System.out.println("\nExecuting chainWithBefore:");
        Integer beforeResult = combinedBefore.get();  // 执行链式调用
        System.out.println(beforeResult);  // 输出：42
    }
}
