package com.bigshen.learningDemo.design.prototype;

/**
 * @Description:正方形
 * @Author: byj
 * @Date: 2019/12/3 15:50
 */
public class Square extends Shape {

    Square(){
        type = "Square";
    }

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }}
