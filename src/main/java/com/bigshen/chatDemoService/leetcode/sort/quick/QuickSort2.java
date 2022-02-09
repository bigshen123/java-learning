package com.bigshen.chatDemoService.leetcode.sort.quick;

import java.util.Arrays;

/**
 * @Description:快速排序算法
 * 1、以第一个元素为基准数 把数组分为两部分左侧的数小于基准数，右边的数大于基准数
 *     1.1、先从左到右找到一个大于基准数的下标i，再从右往左找到一个小于基准数的下标j
 *     1.2、比较i和j如果i<j则交换位置。
 * 2、递归执行上面方法直到i=j
 * 3、最后将基准值与i和j相等位置的数字交换
 * @Author: BIGSHEN
 * @Date: 2020/1/8 20:49
 */
public class QuickSort2 {
    private static void quickSort(int[] arr, int low, int high){
        int i,j,temp,t;
        if(low>high){
            return;
        }
        i=low;
        j=high;
        //temp就是基准位
        temp = arr[low];

        while (i<j) {
            //先看右边，依次往左递减
            while (temp<=arr[j] && i<j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp>=arr[i] && i<j) {
                i++;
            }
            //如果满足条件则交换
            if (i<j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        //最后将基准值与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        //递归调用左半数组
        quickSort(arr, low, j-1);
        //递归调用右半数组
        quickSort(arr, j+1, high);
    }


    public static void main(String[] args){
        int[] arr = {10,7,2,4,7,62,3,4,2,1,8,9,19};
        System.out.println("排序之前："+Arrays.toString(arr));
        quickSort(arr, 0, arr.length-1);
        System.out.println("排序之后："+Arrays.toString(arr));
    }

}
