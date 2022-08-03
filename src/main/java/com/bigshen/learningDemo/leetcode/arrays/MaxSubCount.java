package com.bigshen.learningDemo.leetcode.arrays;

/**
 * @Description:
 * 给定一个整数数组，查找连续的最大和的子数组(至少包含一个数字)并返回其和。
 *给定输入：[-2,1,-9,4,-1,2,1,-8,4]
 * 输出： 6
 * @Author: BIGSHEN
 * @Date: 2019/12/20 20:28
 */
public class MaxSubCount {
    /**
     * O(n)
     * @param nums
     * @return
     */
    private static int maxSubCount(int[] nums){
        int maxCount=nums[0];
        int max=nums[0];
        for (int num : nums) {
            maxCount = Math.max(num, maxCount + num);
            max = Math.max(max, maxCount);
        }
        return max;
    }

    /**
     * O(n^3)
     * @param a
     * @return
     */
    private static int maxSubArray(int[] a) {
        int sum=0,maxsum=0;
        for(int i=0; i<a.length; i++){
            for(int j=i; j<a.length; j++){
                sum=0;
                for(int k=i; k<j; k++){
                    sum += a[k];
                }
                if(sum > maxsum){
                    maxsum = sum;
                }
            }
        }
        return maxsum;
    }

    /**
     * O(n^2)
     * @param a
     * @return
     */
    private int maxSubArray2(int[] a) {
        int max=Integer.MIN_VALUE;
        for(int i=0; i<a.length; i++){
            int sum = 0;
            for(int j=i; j<a.length; j++){
                sum += a[j];
                if(sum > max){
                    max = sum;
                }
            }
        }
        return max;
    }

    public static void main(String[] args) {
        int[] arr={-2,1,-9,4,-1,2,1,-8,4};
        System.out.println(maxSubCount(arr));
    }
}
