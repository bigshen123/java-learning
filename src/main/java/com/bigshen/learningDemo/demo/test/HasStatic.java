package com.bigshen.learningDemo.demo.test;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/2 9:56
 */
public class HasStatic  {
    private static int x = 100;

    public static void main(String[] args) {
        HasStatic hs1 = new HasStatic();
        x++;
        HasStatic hs2 = new HasStatic();
        x++;
        hs1 = new HasStatic();
        x++;
        HasStatic.x--;
        System.out.println("x=" + x);
    }
}
