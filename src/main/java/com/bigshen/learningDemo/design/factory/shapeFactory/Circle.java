package com.bigshen.learningDemo.design.factory.shapeFactory;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/3 14:24
 */
public class Circle implements Shape {
    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}
