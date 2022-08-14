package com.bigshen.learningDemo.design.decorator;

/**
 * @Description:基础形状装饰者类
 * @Author: byj
 * @Date: 2019/12/4 15:11
 */
public abstract class BaseShapeDecorator implements Shape {
    Shape decoratedShape;

    BaseShapeDecorator(Shape decoratedShape){
        this.decoratedShape = decoratedShape;
    }

    @Override
    public void draw(){
        decoratedShape.draw();
    }
}
