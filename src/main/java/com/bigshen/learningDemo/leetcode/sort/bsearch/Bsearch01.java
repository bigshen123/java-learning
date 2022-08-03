package com.bigshen.learningDemo.leetcode.sort.bsearch;

/**
 * @ClassName Bsearch01
 * @Description: TODO 二分查找
 * @Author BYJ
 * @Date 2020/10/9
 * @Version V1.0
 **/
public class Bsearch01{

    /**
     * 简单实现二分查找
     * @param a 数组
     * @param n 数组总个数
     * @param value 目标值
     * @return 目标值下标索引
     */
        public static int bsearch01(int[] a, int n, int value) {
            int low = 0;
            int high = n - 1;

            while (low <= high) {
                int mid = (low + high) / 2;
                if (a[mid] == value) {
                    return mid;
                } else if (a[mid] < value) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }

            return -1;
        }

    /**
     * 递归实现
     * @param a
     * @param n
     * @param val
     * @return
     */
    public static int bsearch02(int[] a, int n, int val) {
        return bsearchInternally(a, 0, n - 1, val);
    }

    private static int bsearchInternally(int[] a, int low, int high, int value) {
        if (low > high) return -1;

        int mid =  low + ((high - low) >> 1);
        if (a[mid] == value) {
            return mid;
        } else if (a[mid] < value) {
            return bsearchInternally(a, mid+1, high, value);
        } else {
            return bsearchInternally(a, low, mid-1, value);
        }
    }


    public static void main(String[] args) {
        int a[]={1,3,6,9,12,45};
        //int i = bsearch01(a, 6, 9);
        int i = bsearch02(a, 6, 9);
        System.out.println(i);
    }


}
