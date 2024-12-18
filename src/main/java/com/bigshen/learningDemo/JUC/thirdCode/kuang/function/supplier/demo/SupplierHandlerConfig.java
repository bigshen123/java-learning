package com.bigshen.learningDemo.JUC.thirdCode.kuang.function.supplier.demo;

import lombok.Getter;

import java.util.function.Supplier;

/**
 * @author byj
 * @date 2024/12/18
 * @Description Supplier 接口用于获取结果，它没有参数，仅提供返回值。
 * 我们可以创建一个封装类 SupplierHandlerConfig 来管理 Supplier 的执行。
 */
@Getter
public class SupplierHandlerConfig<T> {
    /**
     * 供应者（Supplier）实例
     */
    private final Supplier<T> supplier;

    public SupplierHandlerConfig(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    /**
     * 组合两个 Supplier，使得返回结果为第一个 Supplier 和第二个 Supplier 结果的结合
     *
     * @param after 第二个 Supplier
     * @return 返回组合后的 Supplier
     */
    public Supplier<T> chainWithAfter(Supplier<T> after) {
        return () -> {
            T firstResult = supplier.get();
            T secondResult = after.get();
            return secondResult; // 这里只返回第二个 Supplier 的结果
        };
    }

    /**
     * 组合两个 Supplier，使得返回结果为第一个 Supplier 和第二个 Supplier 结果的结合
     *
     * @param before 第一个 Supplier
     * @return 返回组合后的 Supplier
     */
    public Supplier<T> chainWithBefore(Supplier<T> before) {
        return () -> {
            T firstResult = before.get();
            T secondResult = supplier.get();
            return secondResult; // 这里只返回第二个 Supplier 的结果
        };
    }
}
