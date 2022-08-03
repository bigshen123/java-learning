package com.bigshen.learningDemo.design.strategy.operation;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 16:08
 */
public class OperationAdd implements Strategy {
    @Override
    public int doOperation(int num1, int num2) {
        return num1+num2;
    }
}
