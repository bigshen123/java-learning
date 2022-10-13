package com.bigshen.learningDemo.demo.copy.deapCopy.clone;

/**
 * @author byj
 * @date 2022/10/13
 */
class School implements Cloneable {
    int age;                 //基本数据类型
    String name;             //String引用类型
    Subject subject;         //类的对象，引用类型

    // 深拷贝，循环写clone
    @Override
    protected Object clone() throws CloneNotSupportedException {
        School deepSchool = null;
        try {
            deepSchool = (School) super.clone();
            deepSchool.subject = (Subject) deepSchool.subject.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        return deepSchool;
    }

    @Override
    public String toString() {
        return "School{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", subject=" + subject +
                '}';
    }
}
