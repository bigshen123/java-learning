package com.bigshen.learningDemo.leetcode;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @ClassName TwoSum
 * @Description:TODO
 * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
 *
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素不能使用两遍。
 * 例子：
 * 给定 nums = [2, 7, 11, 15], target = 9
 *
 * 因为 nums[0] + nums[1] = 2 + 7 = 9
 * 所以返回 [0, 1]
 * @Author: byj
 * @Date: 2020/12/2
 */
public class TwoSum {
    public static int[] twoSum(int[] nums, int target) {
        int[] indexs = new int[2];
        for (int i = 0; i < nums.length; i++) {
            for (int j = nums.length-1; j > i; j--) {
                if (nums[i]+nums[j]==target){
                    indexs[0]=i;
                    indexs[1]=j;
                    //return indexs;
                }
            }
        }
        return indexs;
    }
    public static int[] twoSum02(int[] nums, int target) {
        int[] indexs = new int[2];
        // 建立k-v ，一一对应的哈希表
        HashMap<Integer,Integer> hash = new HashMap<Integer,Integer>();
        for (int i = 0; i < nums.length; i++) {
            if (hash.containsKey(nums[i])){
                indexs[0]=i;
                indexs[1]=hash.get(nums[i]);
                return indexs;
            }
            // 将数据存入 key为补数 ，value为下标
            hash.put(target-nums[i],i);
        }
        return indexs;
    }

    public static void main(String[] args) {
        int[] nums={3,3};
        int[] ints = twoSum(nums, 6);
        System.out.println(Arrays.toString(ints));
    }
}
