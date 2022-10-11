package com.bigshen.learningDemo.design.factory.shapeFactory;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/3 14:23
 */
public class Rectangle implements Shape {
    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }
}
