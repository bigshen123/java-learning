package com.bigshen.learningDemo.leetcode.arrays;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName RemoveDuplicates
 * @Description: TODO
 * 给定一个排序数组，你需要在 原地 删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
 *
 * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
 *给定 nums = [0,0,1,1,1,2,2,3,3,4],
 *
 * 函数应该返回新的长度 5, 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4。
 *
 * 你不需要考虑数组中超出新长度后面的元素。
 *
 * @Author BYJ
 * @Date 2020/5/27
 * @Version V1.0
 **/
public class RemoveDuplicates {
    public static int removeDuplicates(int[] nums) {
        int index=0;
        Set<Integer> set=new HashSet<>();
        for (int i : nums) {
            if (!set.contains(i)){
                nums[index++]=i;
                set.add(i);
            }
        }
        return index;
    }
    public static int removeDuplicates2(int[] nums) {
       int index=1;
       if (nums.length<=1) {
           return nums.length;
       }
        for (int i = 1; i < nums.length; i++) {
            if (nums[index-1]!=nums[i]){
                nums[index++]=nums[i];
            }
        }
        return index;
    }

    public static void main(String[] args) {
        int[] nums={0,0,1,1,1,2,2,3,3,4};
        System.out.println(removeDuplicates2(nums));
    }
}
