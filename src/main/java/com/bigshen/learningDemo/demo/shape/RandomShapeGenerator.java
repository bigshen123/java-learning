package com.bigshen.learningDemo.demo.shape;

import java.util.Random;

/**
 * @Description:形状随机生成器
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:23
 */
class RandomShapeGenerator {
    private Random random=new Random(100);
    Shape next(){
        switch (random.nextInt(3)){
            default:
            case 0:return new Circle();
            case 1:return new Square();
            case 2:return new Triangle();
        }
    }
}
