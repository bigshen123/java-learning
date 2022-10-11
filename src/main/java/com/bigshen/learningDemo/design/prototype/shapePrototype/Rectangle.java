package com.bigshen.learningDemo.design.prototype.shapePrototype;

/**
 * @Description:矩形
 * @Author: byj
 * @Date: 2019/12/3 15:49
 */
public class Rectangle extends Shape {

    Rectangle(){
        type = "Rectangle";
    }

    @Override
    public void draw() {
        System.out.println("Inside Rectangle::draw() method.");
    }}