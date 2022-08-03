package com.bigshen.learningDemo.design.abstractFactory;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/3 14:33
 */
public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}