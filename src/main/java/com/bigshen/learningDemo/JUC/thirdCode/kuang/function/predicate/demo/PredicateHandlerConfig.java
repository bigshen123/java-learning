package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.predicate.demo;

import org.apache.poi.ss.formula.functions.T;

import java.util.function.Predicate;

/**
 * @author byj
 * @date 2024/12/18
 * @Description
 */
public class PredicateHandlerConfig<T> {

    /**
     * Predicate实例
     */
    private final Predicate<T> predicate;

    public PredicateHandlerConfig(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public Predicate<T> getPredicate() {
        return predicate;
    }

    /**
     * 使用 and 组合两个 Predicate
     * @param other 另一个 Predicate
     * @return 组合后的 Predicate
     */
    public Predicate<T> and(Predicate<T> other) {
        return predicate.and(other);
    }

    /**
     * 使用 or 组合两个 Predicate
     * @param other 另一个 Predicate
     * @return 组合后的 Predicate
     */
    public Predicate<T> or(Predicate<T> other) {
        return predicate.or(other);
    }

    /**
     * 反转 Predicate 的结果
     * @return 反转后的 Predicate
     */
    public Predicate<T> negate() {
        return predicate.negate();
    }

    /**
     * 判断两个对象是否相等
     * @param other 另一个对象
     * @return 是否相等的 Predicate
     */
    public static <T> Predicate<T> isEqual(Object other) {
        return Predicate.isEqual(other);
    }
}
