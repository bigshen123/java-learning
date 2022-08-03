package com.bigshen.learningDemo.leetcode.reverse;

/**
 * @ClassName Reverse
 * @Description: TODO
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
 * 示例 1:
 *
 * 输入: 123
 * 输出: 321
 *  示例 2:
 *
 * 输入: -123
 * 输出: -321
 * 示例 3:
 *
 * 输入: 120
 * 输出: 21
 * 假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−231,  231 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0。
 * @Author BYJ
 * @Date 2020/5/25
 * @Version V1.0
 **/
public class Reverse {
    public static int reverse(int x) {
        long n = 0;
        while(x != 0) {
            n = n*10 + x%10;
            x = x/10;
        }
        return (int)n==n? (int)n:0;
    }
    public static int reverse2(int x) {
        long rst=0;
        while(x!=0){
            rst= rst*10+ x%10;
            x=x/10;

        }
        if((int)rst!=rst){
            return 0;

        }else{
            return (int)rst;
        }
    }
    public static void main(String[] args) {
        int reverse = reverse(322132131);
        System.out.println(reverse);
    }
}
