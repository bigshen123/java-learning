package com.bigshen.learningDemo.demo.test;

/**
 * @Description:九九乘法表
 * @Author: BIGSHEN
 * @Date: 2019/12/21 17:00
 */
public class NineNineMultiplication {
    public static void main(String[] args) {
        for (int i = 1; i <= 9; i++) {
            for (int j = 1; j <= i; j++) {
                System.out.print(i+"*"+j+"="+i*j+"\t");
            }
            System.out.println("");
        }
    }
}
