package com.bigshen.learningDemo.design.factory;

/**
 * @Description:工厂设计模式
 * @Author: byj
 * @Date: 2019/12/3 14:24
 */
class ShapeFactory {
    /**
     * 使用 getShape 方法获取形状类型的对象
     * @param shapeType
     * @return
     */
    Shape getShape(String shapeType) {
        if (shapeType == null) {
            return null;
        }
        if ("CIRCLE".equalsIgnoreCase(shapeType)) {
            return new Circle();
        } else if ("RECTANGLE".equalsIgnoreCase(shapeType)) {
            return new Rectangle();
        } else if ("SQUARE".equalsIgnoreCase(shapeType)) {
            return new Square();
        }
        return null;
    }
}
