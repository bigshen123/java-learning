package com.bigshen.learningDemo.design.builder;

/**
 * @Description:瓶装
 * @Author: byj
 * @Date: 2019/12/3 15:12
 */

public class Bottle implements Packing {

    @Override
    public String pack() {
        return "Bottle";
    }
}
