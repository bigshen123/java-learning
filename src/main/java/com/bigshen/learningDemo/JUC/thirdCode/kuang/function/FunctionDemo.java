package com.bigshen.learningDemo.JUC.thirdCode.kuang.function;

/**
 * @author byj
 * @date 2024/11/25
 * @Description
 * Function接口，接口用来根据一个类型的数据得到另一个类型的数据，前者称为前置条件，后者称为后置条件。有参数有返回值。
 */
@FunctionalInterface
interface Hello {
    void sayHello(String msg);
}

public class FunctionDemo {
    public static void main(String[] args) {
        // 不用括号
        Hello world = message ->
                System.out.println("Hello " + message);
        // 用括号
        Hello person = (message) ->
                System.out.println("Hello " + message);
        world.sayHello("World!");
        person.sayHello("Person!");
    }
}
