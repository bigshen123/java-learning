package com.bigshen.learningDemo.lambda.functional;

/**
 * @ClassName TransformFunction
 * @Description:TODO
 * 这里使用到了 Function 接口中名为 andThen() 的默认方法，该方法专门用于操作函数。 顾名思义，在调用 in 函数之后调用 andThen()（还有个 compose() 方法，
 * 它在 in 函数之前应用新函数）。
 * 要附加一个 andThen() 函数，我们只需将该函数作为参数传递。 transform() 产生的是一个新函数，它将 in 的动作与 andThen() 参数的动作结合起来。
 * @Author: byj
 * @Date: 2020/8/4
 */

import java.util.function.Function;

class I {
    @Override
    public String toString() { return "I"; }
}

class O {
    @Override
    public String toString() { return "O"; }
}

public class TransformFunction {
    static Function<I,O> transform(Function<I,O> in) {
        return in.andThen(o -> {
            System.out.println(o);
            return o;
        });
    }
    public static void main(String[] args) {
        Function<I,O> f2 = transform(i -> {
            System.out.println(i);
            return new O();
        });
        O o = f2.apply(new I());
    }
}
