package com.bigshen.learningDemo.demo.shape;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:24
 */
public class Triangle extends Shape {
    @Override
    public void draw() {
        System.out.println("Triangle...draw...");
    }

    @Override
    public void erase() {
        System.out.println("Triangle...erase...");
    }
}
