package com.bigshen.learningDemo.demo.test;

/**
 * @ClassName StringEqualTest
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/8/31
 * @Version V1.0
 **/
public class StringEqualTest {
    public static void main(String[] args) {
        String a="Bigshen";
        String b=new String("Bigshen");
        String c="Big"+"shen";

        System.out.println(a==b);//false
        System.out.println(a==c);//true
        System.out.println(a.equals(b));//true
        System.out.println(a.equals(c));//true
        System.out.println(a.intern() == b.intern());//true
    }
}
