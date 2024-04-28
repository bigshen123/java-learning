package com.bigshen.learningDemo.javaSE.copy.shallowCopy;

/**
 * @author byj
 * @date 2022/10/13
 */
class School implements Cloneable {
    int age;                 //基本数据类型
    String name;             //String引用类型
    Subject subject;         //类的对象，引用类型

    // 由于 Object 本身没有实现 Cloneable 接口
    // 所以不重写 clone 方法并且进行调用的话会发生 CloneNotSupportedException 异常。
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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
