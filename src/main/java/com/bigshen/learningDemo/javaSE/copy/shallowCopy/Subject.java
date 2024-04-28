package com.bigshen.learningDemo.javaSE.copy.shallowCopy;

/**
 * @author byj
 * @date 2022/10/13
 */
public class Subject {
    String name;    //String引用类型
    int classNum;   //基本数据类型

    public Subject(String name, int classNum) {
        this.name = name;
        this.classNum = classNum;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "name='" + name + '\'' +
                ", classNum=" + classNum +
                '}';
    }
}
