package com.bigshen.learningDemo.javaSE.string.split;

import java.util.Scanner;

/**
 * @Description:输入一串字符 按照指定字节数截取展示
 * @Author: byj
 * @Date: 2019/12/5 14:43
 */
public class InterceptionStr {
    //输入的字符
    private static String ss;
    //设定字节数
    private static int n;

    /**
     * 主方法
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("请输入字符数");
        Scanner scStr = new Scanner(System.in);
        ss = scStr.next();
        System.out.println("请输入字节数");
        Scanner scInt = new Scanner(System.in);
        n = scInt.nextInt();
        Interception(setValue());

    }

    /**
     * 此方法的作用是将字符串转换成字符串数组
     * @return
     */
    private static String[] setValue() {
        String[] strings = new String[ss.length()];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = ss.substring(i, i + 1);
        }
        return strings;
    }

    /**
     * 截取策略
     * @param strings
     */
    private static void Interception(String[] strings) {
        int count = 0;
        String m = "[\u4e00-\u9fa5]";
        System.out.println("以每"+n+"字节划分的字符串如下所示");
        for (String string : strings) {
            if (string.matches(m)) {
                //中文的正则表达式：+2
                count += 2;
            } else {
                //字符的 +1
                count += 1;
            }
            if (count < n) {
                System.out.print(string);
            } else if (count == n) {
                System.out.print(string);
                count = 0;
                System.out.println();
            } else {
                count = 0;
                System.out.println();
            }
        }
    }
}
