package com.bigshen.learningDemo.leetcode;

import java.util.Arrays;

/**
 * @ClassName CountPrimes 计数质数
 * @Description:TODO 统计所有小于非负整数 n 的质数的数量。
 * 示例 1：
 *
 * 输入：n = 10
 * 输出：4
 * 解释：小于 10 的质数一共有 4 个, 它们是 2, 3, 5, 7 。
 * 示例 2：
 *
 * 输入：n = 0
 * 输出：0
 * 示例 3：
 *
 * 输入：n = 1
 * 输出：0
 *  
 *
 * 提示：
 *
 * 0 <= n <= 5 * 106
 *
 * @Author: byj
 * @Date: 2020/12/3
 */
public class CountPrimes {

    public static int countPrimes(int n) {
        if (n < 3) {
            return 0;
        }
        boolean[] f = new boolean[n];
        //count返回值，除去一半偶数，(***把2当作1计数***)，假设剩余全是质数
        //为什么可以直接/2
        //n为12时算的是0~11,设b=11
        //b/2+1即为零到十一的奇数数目=n/2=6

        //n为11时算的是0~10，设b=10
        //b/2即为零到十的奇数数目=n/2=5

        //如果题目说的是小于等于n的话，显然count = (b+1)/2即本题的n/2
        int count = n / 2;
        //从奇数里面过滤，每次加2
        for (int i = 3; i * i < n; i += 2) {
            //表示非质数直接跳过
            //可以跳过的原因是，不是质数肯定可以表示俩个数相乘
            //那么俩个小数结果处理肯定可以涵盖大数的处理
            //比如15，如果已经对3,5处理过的话，15肯定不用再做处理
            if (f[i]) {
                continue;
            }
            //为什么可以从i*i开始而不是i*3(不可能是i*2，偶数在第一步就被过滤掉了)
            //如果j=121
            //那么11*3,11*5,11*7,11*9会不会被漏掉
            //显然不会.....
            for (int j = i * i; j < n; j += 2 * i) {
                if (!f[j]) {
                    --count;
                    //System.out.println(j);
                    f[j] = true;
                }
            }
        }
        return count;
    }

    /**
     * 记录质数+只看质数做因数的余数
     * @param n
     * @return
     */
    public static int countPrimes02(int n){
        int[] isPrime = new int[n];
        Arrays.fill(isPrime,1);
        int res = 0;
        for (int i = 2; i < n; i++) {
            for (int j = 2; j * j <= i; j++) {
                if(isPrime[j]==1){
                    if (i % j == 0) {
                        isPrime[i]=0;
                        res--;
                        break;
                    }
                }
            }
            res++;
        }
        return res;
    }

    public static void main(String[] args) {
        //int i = countPrimes(10);
        int i = countPrimes02(10);
        System.out.println(i);
    }
}
