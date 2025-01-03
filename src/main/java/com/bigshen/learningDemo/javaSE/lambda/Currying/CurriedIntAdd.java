package com.bigshen.learningDemo.javaSE.lambda.Currying;

/**
 * @ClassName CurriedIntAdd
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/8/4
 */

import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

public class CurriedIntAdd {
    public static void main(String[] args) {
        IntFunction<IntUnaryOperator>
                curriedIntAdd = a -> b -> a + b;
        IntUnaryOperator add4 = curriedIntAdd.apply(4);
        System.out.println(add4.applyAsInt(5));
    }
}
