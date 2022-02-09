package com.bigshen.chatDemoService.leetcode.sort.bsearch;

import java.util.Arrays;
import java.util.Collections;

/**
 * @Description:二分排序
 *
 * @Author: BIGSHEN
 * @Date: 2019/12/14 12:58
 */
public class BiSearchSort {
    /**
     * 二分查找查找指定值的索引
     * @param arrs  有序数组
     * @param key   目标数值
     * @param L     左边 index
     * @param R     右边 index
     * @return      返回值 查找不到就是 -1
     */
    public static int choose(int[] arrs,int key,int L,int R){
        while(L<=R){
            int minIndex=(L+R)/2;
            if(arrs[minIndex]<key){
                L=++minIndex;
            }else if(arrs[minIndex]>key){
                R=--minIndex;
            }else {
                return minIndex;//返回目标数值的索引
            }
        }
        return -1;//如果未找到目标数值就返回 -1
    }

    /**
     * 二分排序非递归算法
     * @param a {1, 100, 4, 7, 77, 16, 33, 43, 99, 11, 88, 90, 65, 3};
     */
    private static void binarySort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int tmp = a[i];
            int left = 0;
            int right = i - 1;

            while (left <= right) {
                int mid = (right + left)>>1;
                if (a[mid] > tmp) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            //destpos:目标数据中的起始位置
            if (i - left >= 0) System.arraycopy(a, left, a, left + 1, i - left);
            a[left] = tmp;
        }
    }

    /**
     * 二分查找
     * 又叫折半查找，要求待查找的序列有序。每次取中间位置的值与待查关键字比较，如果中间位置
     *  * 的值比待查关键字大，则在前半部分循环这个查找的过程，如果中间位置的值比待查关键字小，
     *  * 则在后半部分循环这个查找的过程。直到查找到了为止，否则序列中没有待查的关键字
     * @param array
     * @param a
     * @return
     */
    public static int biSearch(int []array,int a){
        int lo=0;
        int hi=array.length-1;
        int mid;
        while(lo<=hi){
            mid=(lo+hi)/2;//中间位置
            if(array[mid]==a){
                return mid+1;
            }else if(array[mid]<a){ //向右查找
                lo=mid+1;
            }else{ //向左查找
                hi=mid-1;
            }
        }
        return -1;
    }
    public static void main(String[] args) {
        int[] arr = {1, 100, 4, 7, 77, 16, 33, 43, 99, 11, 88, 90, 65, 3};
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));
        //int choose = choose(arr, 33, 0, arr.length - 1);
        //System.out.println(choose);
        //binarySort(arr);
        int i = biSearch(arr, 33);
        System.out.println(i);
        //System.out.println(Arrays.toString(arr));
    }
}
