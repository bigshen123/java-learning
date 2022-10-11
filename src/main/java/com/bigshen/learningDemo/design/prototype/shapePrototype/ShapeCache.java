package com.bigshen.learningDemo.design.prototype.shapePrototype;

import java.util.Hashtable;

/**
 * @Description:该类把 shape 对象存储在一个 Hashtable 中，并在请求的时候返回它们的克隆
 * @Author: byj
 * @Date: 2019/12/3 15:50
 */
class ShapeCache {

    private static Hashtable<String, Shape> shapeMap = new Hashtable<String, Shape>();

    static Shape getShape(String shapeId) {
        Shape cachedShape = shapeMap.get(shapeId);
        return (Shape) cachedShape.clone();
    }

    /**
     *   对每种形状都运行数据库查询，并创建该形状
     *  shapeMap.put(shapeKey, shape);
     *  例如，我们要添加三种形状
     */
    static void loadCache() {
        Circle circle = new Circle();
        circle.setId("1");
        shapeMap.put(circle.getId(),circle);

        Square square = new Square();
        square.setId("2");
        shapeMap.put(square.getId(),square);

        Rectangle rectangle = new Rectangle();
        rectangle.setId("3");
        shapeMap.put(rectangle.getId(),rectangle);
    }
}
