package com.bigshen.learningDemo.design.decorator;

/**
 * @Description:红色形状装饰者类
 * @Author: byj
 * @Date: 2019/12/4 15:12
 */
public class RedShapeDecorator extends BaseShapeDecorator {

    RedShapeDecorator(Shape decoratedShape) {
        super(decoratedShape);
    }

    @Override
    public void draw() {
        decoratedShape.draw();
        setRedBorder(decoratedShape);
    }

    private void setRedBorder(Shape decoratedShape){
        System.out.println("Border Color: Red");
    }
}
