package com.bigshen.learningDemo.design.strategy.operation;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:11
 */
public class StrategyPatternDemo {
    public static void main(String[] args) {
        Context context = new Context(new OperationAdd());
        System.out.println("10 + 5 = " + context.executeStrategy(10, 5));

        context = new Context(new OperationSubstract());
        System.out.println("20 - 10 = " + context.executeStrategy(20, 10));

        context = new Context(new OperationMultiply());
        System.out.println("10 * 5 = " + context.executeStrategy(10, 5));
    }
}
