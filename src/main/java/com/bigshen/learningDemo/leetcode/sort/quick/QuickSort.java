package com.bigshen.learningDemo.leetcode.sort.quick;

import java.util.Arrays;

/**
 * @Description:快速排序
 * @Author: BIGSHEN
 * @Date: 2019/12/19 19:24
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] arr = {12, 2, 45, 6, 34, 77, 4, 22, 54, 36};
        System.out.println("排序前的数组：" + Arrays.toString(arr));
        int[] array = quickSort(arr, 0, arr.length-1);
        System.out.println("排序后的数组：" + Arrays.toString(array));
    }

    /**
     * 快速排序方法
     * @param array 数组
     * @param start 要排序的开始索引
     * @param end 要排序的结束索引
     * @return
     */
    private static int[] quickSort(int[] array, int start, int end) {
        if (array.length < 1 || start < 0 || end >= array.length || start > end) {
            return null;
        }
        //根据传入的开始、结束索引
        int smallIndex = partition(array, start, end);
        if (smallIndex > start) {
            quickSort(array, start, smallIndex - 1);
        }
        if (smallIndex < end) {
            quickSort(array, smallIndex + 1, end);
        }
        return array;
    }
    /**
     * 快速排序算法——partition分区
     * @param array{12, 2, 45, 6, 34, 77, 4, 22, 54, 36}
     * @param start
     * @param end
     * @return//;
     */
    private static int partition(int[] array, int start, int end) {
        //随机选择一段区域
        int pivot = (int) (start + Math.random() * (end - start + 1));
        int smallIndex = start - 1;
        swap(array, pivot, end);
        for (int i = start; i <= end; i++) {
            if (array[i] <= array[end]) {
                smallIndex++;
                if (i > smallIndex) {
                    swap(array, i, smallIndex);
                }
            }
        }
        return smallIndex;
    }

    /**
     * 交换数组内两个元素
     * @param array
     * @param i
     * @param j
     */
    private static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
