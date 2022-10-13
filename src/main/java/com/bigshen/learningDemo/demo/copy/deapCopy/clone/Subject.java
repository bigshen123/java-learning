package com.bigshen.learningDemo.demo.copy.deapCopy.clone;

/**
 * @author byj
 * @date 2022/10/13
 */
public class Subject implements Cloneable{
    String name;    //String引用类型
    int classNum;   //基本数据类型

    public Subject(String name, int classNum) {
        this.name = name;
        this.classNum = classNum;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", classNum=" + classNum +
                '}';
    }
}
