package com.bigshen.learningDemo.leetcode.sorted;

/**
 * @Description:查询数组中位数并且时间复杂度O（log（m+n））
 * @Author: BIGSHEN
 * @Date: 2019/12/8 7:59
 */
public class FindMedianSorted02 {
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int left = (m + n + 1) / 2;
        int right = (m + n + 2) / 2;
        return (findKth(nums1, 0, nums2, 0, left) + findKth(nums1, 0, nums2, 0, right)) / 2.0;
    }

    /**
     * @param nums1：数组1
     * @param i : nums1的起始位置
     * @param nums2：数组2
     * @param j : nums2的起始位置
     * @param k ：数组之和为奇数则表示中位数，数组之和为偶数则表示左侧中位数或右侧中位数
     * @return
     */
    private static int findKth(int[] nums1, int i, int[] nums2, int j, int k){
        if( i >= nums1.length) return nums2[j + k - 1];//nums1为空数组
        if( j >= nums2.length) return nums1[i + k - 1];//nums2为空数组
        if(k == 1){
            return Math.min(nums1[i], nums2[j]);
        }
        int midVal1 = (i + k / 2 - 1 < nums1.length) ? nums1[i + k / 2 - 1] : Integer.MAX_VALUE;
        int midVal2 = (j + k / 2 - 1 < nums2.length) ? nums2[j + k / 2 - 1] : Integer.MAX_VALUE;
        if(midVal1 < midVal2){
            return findKth(nums1, i + k / 2, nums2, j , k - k / 2);
        }else{
            return findKth(nums1, i, nums2, j + k / 2 , k - k / 2);
        }
    }

    public static void main(String[] args) {
        int[] a={1,2,3,32,22,4};
        int[] b={6,7,8,9};
        double medianSortedArrays = findMedianSortedArrays(a, b);
        System.out.println(medianSortedArrays);
    }
}
