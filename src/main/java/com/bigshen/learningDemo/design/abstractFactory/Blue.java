package com.bigshen.learningDemo.design.abstractFactory;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/3 14:34
 */
public class Blue implements Color {

    @Override
    public void fill() {
        System.out.println("Inside Blue::fill() method.");
    }
}