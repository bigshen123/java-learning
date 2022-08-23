package com.bigshen.learningDemo.leetcode;

import java.util.Scanner;

/**
 * @author byj
 * @date 2022/8/22
 * 输入一个日期，输出下一个为素数的日期
 */
public class Tidy {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.println("输入一个数：");
            try {
                int num = scan.nextInt();
                for (int i = num + 1; ; i++) {
                    boolean isPrime = true;
                    for (int j = 2; j < i; j++) {
                        if (i % j == 0) {
                            isPrime = false;
                            break;
                        }
                    }
                    if (isPrime) {
                        System.out.println(num + " 后面的第一个素数是: " + i);
                        System.out.println();
                        break;
                    }
                }
            } catch (Exception e) {
                scan.close();
                break;
            }
        }
    }
}
