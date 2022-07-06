package com.bigshen.chatDemoService.demo.test;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/12 16:26
 */
public class URShift {

    public static void main(String[] args) {
        int i=-1;
        System.out.println(Integer.toBinaryString(i));
        i>>>=10;
        System.out.println(Integer.toBinaryString(i));
    }
}
