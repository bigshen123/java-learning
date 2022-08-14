package com.bigshen.learningDemo.design.bridge;

/**
 * @ClassName BridgePatternDemo
 * @Description: TODO  桥接设计模式
 * 使用 Shape 和 DrawAPI 类画出不同颜色的圆
 * @Author BYJ
 * @Date 2020/5/26
 * @Version V1.0
 **/
public class BridgePatternDemo {
    public static void main(String[] args) {
        Shape redCircle = new Circle(100,100, 10, new RedCircle());
        Shape greenCircle = new Circle(100,100, 10, new GreenCircle());

        redCircle.draw();
        greenCircle.draw();
    }
}
