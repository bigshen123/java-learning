package com.bigshen.learningDemo.design.builder.MealBuilder;

/**
 * @Description:鸡肉汉堡
 * @Author: byj
 * @Date: 2019/12/3 15:13
 */
public class ChickenBurger extends Burger {

    @Override
    public float price() {
        return 50.5f;
    }

    @Override
    public String name() {
        return "Chicken Burger";
    }
}
