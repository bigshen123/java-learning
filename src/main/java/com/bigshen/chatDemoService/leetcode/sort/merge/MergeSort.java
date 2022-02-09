package com.bigshen.chatDemoService.leetcode.sort.merge;

import java.util.Arrays;

/**
 * @Description:归并排序
 * @Author: BIGSHEN
 * @Date: 2019/12/19 19:13
 */
public class MergeSort {
    public static void main(String[] args) {
        int[] arr = {12, 2, 45, 6, 34, 77, 4, 22, 54, 36};
        System.out.println("排序前的数组：" + Arrays.toString(arr));
        int[] array = mergeSort(arr);
        System.out.println("排序后的数组：" + Arrays.toString(array));
    }

    /**
     * 归并算法
     * @param array
     * @return
     */
    private static int[] mergeSort(int[] array) {
        if (array.length < 2) {
            return array;
        }
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    /**
     * 进行归并
     * @param left 左集合
     * @param right 右集合
     * @return
     */
    private static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length)
                result[index] = right[j++];
            else if (j >= right.length)
                result[index] = left[i++];
            else if (left[i] > right[j])
                result[index] = right[j++];
            else
                result[index] = left[i++];
        }
        return result;
    }
}
