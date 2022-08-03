package com.bigshen.learningDemo.design.builder;

/**
 * @Description:素食汉堡
 * @Author: byj
 * @Date: 2019/12/3 15:13
 */
public class VegBurger extends Burger {

    @Override
    public float price() {
        return 25.0f;
    }

    @Override
    public String name() {
        return "Veg Burger";
    }
}
