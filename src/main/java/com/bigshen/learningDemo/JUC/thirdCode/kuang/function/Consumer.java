package com.bigshen.learningDemo.JUC.thirdCode.kuang.function;

import java.util.Objects;

/**
 * @author byj
 * @date 2024/11/25
 * @Description 有参无返回值得接口,前面介绍的Supplier接口是用来生产数据的，而Consumer接口是用来消费数据的，使用的时候需要指定一个泛型来定义参数类型

 * 默认方法 andThen：方法的参数和返回值全部是Consumer类型，那么就可以实现效果，消费一个数据的时候，
 * 首先做一个操作，然后再做一个操作，实现组合
 */
@FunctionalInterface
public interface Consumer<T> {
    /*
     ** Performs this operation on the given argument.
     ** @param t the input argument
     */
    void accept(T t);

    default Consumer<T> andThen(Consumer<? super T> after, Consumer<? super T> before) {
        Objects.requireNonNull(after);
        return (T t) -> { before.accept(t);accept(t); after.accept(t); };
    }
}
