package com.bigshen.learningDemo.design.builder;

/**
 * @Description:冷饮
 * @Author: byj
 * @Date: 2019/12/3 15:13
 */
public abstract class ColdDrink implements Item {

    @Override
    public Packing packing() {
        return new Bottle();
    }

    @Override
    public abstract float price();
}
