package com.bigshen.learningDemo.javaSE.lambda.functional;

import java.util.function.BiConsumer;

/**
 * @ClassName MethodConversion
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/8/4
 */
class In1 {}

class In2 {}

public class MethodConversion {
    static void accept(In1 i1, In2 i2) {
        System.out.println("accept()");
    }
    static void someOtherName(In1 i1, In2 i2) {
        System.out.println("someOtherName()");
    }
    public static void main(String[] args) {
        BiConsumer<In1,In2> bic;

        bic = MethodConversion::accept;
        bic.accept(new In1(), new In2());

        bic = MethodConversion::someOtherName;
        // bic.someOtherName(new In1(), new In2()); // Nope
        bic.accept(new In1(), new In2());
    }
}
