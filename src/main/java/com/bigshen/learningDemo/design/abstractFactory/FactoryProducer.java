package com.bigshen.learningDemo.design.abstractFactory;

/**
 * @Description:选择具体工厂类型
 * @Author: byj
 * @Date: 2019/12/3 14:35
 */
class FactoryProducer {
    static AbstractFactory getFactory(String choice){
        if("SHAPE".equalsIgnoreCase(choice)){
            return new ShapeFactory();
        } else if("COLOR".equalsIgnoreCase(choice)){
            return new ColorFactory();
        }
        return null;
    }
}