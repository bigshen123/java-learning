package com.bigshen.learningDemo.design.strategy.operation;

/**
 * @Description: 执行策略操作
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:10
 */
class Context {
    private Strategy strategy;
    Context(Strategy strategy){
        this.strategy=strategy;
    }
    int executeStrategy(int num1, int num2){
        return strategy.doOperation(num1,num2);
    }
}
