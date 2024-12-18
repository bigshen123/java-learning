package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.consumer.demo;

import java.util.function.Consumer;

/**
 * @author byj
 * @date 2024/12/18
 * @Description 对Consumer接口做封装 分别调用其核心方法：

 * accept 有参无返回值
 * andThen 通过 andThen 方法将两个 Consumer 链接起来，顺序执行操作。第一个 Consumer 执行完后，第二个 Consumer 执行。
 */
public class ConsumerHandlerConfigDemo {
    public static void main(String[] args) {

        // 创建一个简单的打印操作的 Consumer
        Consumer<String> printMessage = message -> System.out.println("Message: " + message);

        // 创建一个将消息转为大写后再打印的 Consumer
        Consumer<String> printUpperCaseMessage = message -> System.out.println("UpperCase Message: " + message.toUpperCase());

        // 创建 ConsumerHandlerConfig 配置对象
        ConsumerHandlerConfig<String> messageHandler = new ConsumerHandlerConfig<>(printMessage);

        // 使用链式执行（printMessage -> printUpperCaseMessage）
        Consumer<String> combinedHandler = messageHandler.chainWithAfter(printUpperCaseMessage);

        // 执行操作
        String message = "Hello, World!";
        System.out.println("Executing chainWithAfter:");
        // 输出：Message: Hello, World!  UpperCase Message: HELLO, WORLD!
        combinedHandler.accept(message);

        // 使用 chainWithBefore
        Consumer<String> combinedHandlerBefore = messageHandler.chainWithBefore(printUpperCaseMessage);
        System.out.println("\nExecuting chainWithBefore:");
        // 输出：UpperCase Message: HELLO, WORLD!  Message: Hello, World!
        combinedHandlerBefore.accept(message);
    }
}
