package com.bigshen.chatDemoService.demo.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Description:
 * 从键盘上输入10个整数，并将其放入一个一维数组中然后将其前5个元素与后5个元素对换即：
 * 第1个元素与第10个元素互换，第2个元素与第9个元素互换…
 * 分别输出数组原来各元素的值和对换后各元素的值。
 * @Author: byj
 * @Date: 2019/12/5 15:13
 */
public class HuHuanDemo {

    public static void main(String[] args) {
        print();

    }
    private static void print(){
        int[] ary = write();
        int s;
        System.out.println("\n你输入的数组是：");
        for (int i = 0; i < 10; i++) {
            System.out.println(ary[i]+" ");
        }
        for (int i = 0; i < 5; i++) {
            s=ary[i];
            ary[i]=ary[9-i];
            ary[9-i]=s;
        }
        System.out.println("\n 对换后的数组是：");
        for (int i = 0; i < 10; i++) {
            System.out.println(ary[i]+" ");
        }
        System.out.println();
    }

    private static int[] write(){
        //定义缓冲区数组
        BufferedReader[] buf=new BufferedReader[10];
        int n;
        int array[] = new int[10];
        for (int i = 0; i < 10; i++) {
            buf[i]=new BufferedReader(new InputStreamReader(System.in));
            do {
                n=1;
                System.out.println("请输入第"+(i+1)+"个整数：");
                    try {
                        array[i]= Integer.parseInt(buf[i].readLine());
                    } catch (IOException e) {
                        System.out.println("数据输入错误，请重新输入~");
                        n=0;
                    }
            }while (n==0);
        }
        return array;
    }

}
