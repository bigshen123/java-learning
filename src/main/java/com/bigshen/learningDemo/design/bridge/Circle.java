package com.bigshen.learningDemo.design.bridge;

/**
 * @ClassName Circle
 * @Description: TODO 创建实现了 Shape 接口的实体类
 * @Author BYJ
 * @Date 2020/5/26
 * @Version V1.0
 **/
public class Circle extends Shape {
    private int x, y, radius;

    public Circle(int x, int y, int radius, DrawAPI drawAPI) {
        super(drawAPI);
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void draw() {
        drawAPI.drawCircle(radius,x,y);
    }
}
