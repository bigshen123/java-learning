package com.bigshen.learningDemo.design.prototype;

/**
 * @Description:形状抽象类
 * @Author: byj
 * @Date: 2019/12/3 15:49
 */
public abstract class Shape implements Cloneable{

    private String id;
    String type;

    abstract void draw();

    String getType(){
        return type;
    }

    String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}