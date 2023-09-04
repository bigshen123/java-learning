package com.bigshen.learningDemo.design.builder.DecorationPackage;

/**
 * @Author BYJ
 * @Date 2023/9/4 21:10
 * @Describe
 */
public class Test {

    public static void main(String[] args) {
        Builder builder = new Builder();

        // 豪华欧式
        System.out.println(builder.levelOne(132.52D).getDetail());

        // 轻奢田园
        System.out.println(builder.levelTwo(98.25D).getDetail());

        // 现代简约
        System.out.println(builder.levelThree(85.43D).getDetail());
    }
}
