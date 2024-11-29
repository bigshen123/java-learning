package com.bigshen.learningDemo.JUC.thirdCode.kuang.function;

/**
 * @author byj
 * @date 2024/11/25
 * @Description
 */
@FunctionalInterface
public interface Supplier<T> {
    /**
     * Gets a result.
     ** @return a result
     */
    T get();
}