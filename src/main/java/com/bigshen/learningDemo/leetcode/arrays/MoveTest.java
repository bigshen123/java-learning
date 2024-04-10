package com.bigshen.learningDemo.leetcode.arrays;

import java.util.Arrays;

/**
 * @Author BYJ
 * @Date 2024/3/18 20:41
 * @Describe
 */
public class MoveTest {
    public static void main(String[] args) {
        int[] nums = new int[]{0, 1, 2, 0, 3, 4, 5, 0};
        int[] movedNums = moveZeroes(nums);
        System.out.println(Arrays.toString(movedNums));
    }

    public static int[] moveZeroes(int[] nums) {
        // 记录非零元素应该放置的位置
        int nonZeroIdx = 0;

        // 遍历原始数组，将非零元素移到数组前面
        for (int num : nums) {
            if (num != 0) {
                nums[nonZeroIdx++] = num;
            }
        }

        // 将剩余位置的元素置为零
        while (nonZeroIdx < nums.length) {
            nums[nonZeroIdx++] = 0;
        }

        return nums;
    }
}
