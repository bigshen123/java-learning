package com.bigshen.learningDemo.javaSE.copy.deapCopy.clone;

/**
 * @author byj
 * @date 2022/10/13
 */
public class Demo {
    public static void main(String[] args) {
        School school1 = new School();
        school1.age = 1;
        school1.name = "One";
        school1.subject = new Subject("Java", 1024);
        System.out.println("原先的school1：" + school1);

        School school2 = null;
        try {
            school2 = (School) school1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("原先的school2：" + school2);

        school2.age = 2;                  //改变clone后的age
        school2.name = "Two";             //改变clone后的name
        school2.subject.name = "New";     //改变clone后的subject对象的name
        school2.subject.classNum = 1025;  //改变clone后的subject对象的name
        System.out.println("修改后的school1：" + school1);
        System.out.println("修改后的school2：" + school2);
    }
}
