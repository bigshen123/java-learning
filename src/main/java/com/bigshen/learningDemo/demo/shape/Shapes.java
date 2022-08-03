package com.bigshen.learningDemo.demo.shape;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:22
 */
public class Shapes {
    private static RandomShapeGenerator generator=new RandomShapeGenerator();

    public static void main(String[] args) {
        Shape[] s=new Shape[9];
        for (int i = 0; i < s.length; i++) {
            s[i]=generator.next();
        }
        for (Shape shape : s) {
            shape.draw();
        }
    }
}
