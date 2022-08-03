package com.bigshen.learningDemo.demo.shape;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:24
 */
public class Square extends Shape {
    @Override
    public void draw() {
        System.out.println("Square...draw...");
    }

    @Override
    public void erase() {
        System.out.println("Square...erase...");
    }
}
