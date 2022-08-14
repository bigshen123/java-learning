package com.bigshen.learningDemo.demo.copy;

/**
 * @ClassName CloneExample
 * @Description:TODO
 * @Author: byj
 * @Date: 2020/12/15
 */
public class CloneExample implements Cloneable {

    private int a;
    private int b;
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
