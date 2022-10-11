package com.bigshen.learningDemo.design.abstractFactory.shapeAbstractFactory;

/**
 * @Description:抽象工厂
 * @Author: byj
 * @Date: 2019/12/3 14:34
 */
public abstract class AbstractFactory {
    /**
     * 抽象颜色
     * @param color
     * @return
     */
    abstract Color getColor(String color);

    /**
     * 抽象形状
     * @param shape
     * @return
     */
    abstract Shape getShape(String shape) ;
}