package com.bigshen.learningDemo.leetcode.primeNumber;

/**
 * @Author BYJ
 * @Date 2021/4/11 16:34
 * @Describe 判断素数个数
 */
public class PrimeNumber {
    public static void main(String[] args) {
        System.out.println(bf(100));
        //System.out.println(eratosthenes(100));
    }

    /**
     * 暴力解法
     *
     * @param n
     * @return
     */
    private static int bf(int n) {
        int count = 0;
        for (int i = 2; i < n; i++) {
            count += isPrime(i) ? 1 : 0;
        }
        return count;
    }

    private static boolean isPrime(int x) {
        for (int i = 2; i * i  <= x; i++) {
            if (x % i == 0) {
                return false;
            }
        }
        System.out.println(x);
        return true;
    }

    /**
     * 埃氏筛选(素数和非素数（合数）做不同的标记)
     *
     * @param n
     * @return
     */
    private static int eratosthenes(int n) {
        //false代表素数
        boolean[] isPrime=new boolean[n];
        int count=0;
        for (int i = 2; i < n; i++) {
            if (!isPrime[i]){
                count++;
                //j就是合数的标记位
                for (int j = 2*i; j < n; j+=i) {
                    isPrime[j]=true;
                }
            }
        }
        return count;
    }
}
