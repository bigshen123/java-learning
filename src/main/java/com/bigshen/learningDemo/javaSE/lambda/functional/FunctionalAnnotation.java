package com.bigshen.learningDemo.javaSE.lambda.functional;

/**
 * @ClassName FunctionalAnnotation
 * @Description:TODO  函数式接口 @FunctionalInterface 注解是可选的; Java 在 main() 中把 Functional 和 FunctionalNoAnn 都当作函数式接口。
 * 在 NotFunctional 的定义中可看到@FunctionalInterface 的作用：接口中如果有多个方法则会产生编译期错误。
 * @Author: byj
 * @Date: 2020/8/4
 */
public class FunctionalAnnotation {
    public String goodbye(String arg) {
        return "Goodbye, " + arg;
    }

    @FunctionalInterface
    interface Functional {
        String goodbye(String arg);
    }

    interface FunctionalNoAnn {
        String goodbye(String arg);
    }

/*
@FunctionalInterface
interface NotFunctional {
  String goodbye(String arg);
  String hello(String arg);
}
产生错误信息:
NotFunctional is not a functional interface
multiple non-overriding abstract methods
found in interface NotFunctional
*/
public static void main(String[] args) {
    FunctionalAnnotation fa =
            new FunctionalAnnotation();
    Functional f = fa::goodbye;
    FunctionalNoAnn fna = fa::goodbye;
    // Functional fac = fa; // Incompatible
    Functional fl = a -> "Goodbye, " + a;
    FunctionalNoAnn fnal = a -> "Goodbye, " + a;
}
}
