package com.bigshen.learningDemo.demo.test;

import java.util.Scanner;

/**
 * @Description:电话扣费计数器
 * 电话计费器，输入通话秒数，按分钟计费。（每分钟0.5元）
 * @Author: BIGSHEN
 * @Date: 2019/12/13 19:21
 */
public class PhoneCount {
    public static void main(String[] args) {
        Scanner number=new Scanner(System.in);
        System.out.println("请输入通话秒数：");
        double num = number.nextInt();
        double cost=0;
        if (num % 6==0){
            cost=num/60*0.5;
        }else{
            cost=(int)(num/60+1)*0.5;
        }
        System.out.println("本次通话花费"+cost+"元");
        number.close();
    }
}
