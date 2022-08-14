package com.bigshen.learningDemo.design.bridge;

/**
 * @ClassName Shape
 * @Description: TODO 使用 DrawAPI 接口创建抽象类 Shape
 * @Author BYJ
 * @Date 2020/5/26
 * @Version V1.0
 **/
public abstract class Shape {
    protected DrawAPI drawAPI;
    protected Shape(DrawAPI drawAPI){
        this.drawAPI = drawAPI;
    }
    public abstract void draw();
}