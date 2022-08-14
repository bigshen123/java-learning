package com.bigshen.learningDemo.leetcode.sort.insert;

/**
 * @Author BYJ
 * @Date 2021/3/24 20:32
 * @Describe 直接插入排序
 * 基本思想：每步将一个待排序的记录，按其顺序码大小插入到前面已经排序的字序列的合适位置（从后向前找到合适位置后），直到全部插入排序完为止。
 */
public class DirectInsertSort {
    public static void main(String[] args) {
        int[] a = {49, 38, 65, 97, 76, 13, 27, 49, 78, 34, 12, 64, 1};
        System.out.println("排序之前：");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        // 直接插入排序
        for (int i = 1; i < a.length; i++) {
            int temp=a[i];
            int j;
            for (j = i-1; j >= 0; j--) {
                if (a[j]>temp){
                    a[j+1]=a[j];
                }else {
                    break;
                }
            }
            a[j+1]=temp;
        }
        System.out.println();
        System.out.println("排序之后：");
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i] + " ");
        }
        System.out.println();
    }
}
