package com.bigshen.learningDemo.design.builder.MealBuilder;

/**
 * @Description:汉堡
 * @Author: byj
 * @Date: 2019/12/3 15:12
 */
public abstract class Burger implements Item {

    @Override
    public Packing packing() {
        return new Wrapper();
    }

    @Override
    public abstract float price();
}
