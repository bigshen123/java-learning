package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.consumer.demo;

import lombok.Getter;
import org.apache.poi.ss.formula.functions.T;

import java.util.function.Consumer;

/**
 * @author byj
 * @date 2024/12/18
 * @Description
 */
@Getter
public class ConsumerHandlerConfig<T> {
    /**
     * 请求处理函数（Consumer）
     */
    private final Consumer<T> handler;

    public ConsumerHandlerConfig(Consumer<T> handler) {
        this.handler = handler;
    }

    /**
     * 演示 andThen 使用多个 Consumer 链接执行
     */
    public Consumer<T> chainWithAfter(Consumer<T> after) {
        return this.handler.andThen(after);
    }

    /**
     * 演示 andThen 使用多个 Consumer 先后执行（通过 before）
     */
    public Consumer<T> chainWithBefore(Consumer<T> before) {
        return (T t) -> {
            before.accept(t);
            handler.accept(t);
        };
    }

}
