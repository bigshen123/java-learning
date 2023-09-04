package com.bigshen.learningDemo.design.builder.MealBuilder;

/**
 * @Description:可口可乐
 * @Author: byj
 * @Date: 2019/12/3 15:13
 */
public class Coke extends ColdDrink {

    @Override
    public float price() {
        return 30.0f;
    }

    @Override
    public String name() {
        return "Coke";
    }
}