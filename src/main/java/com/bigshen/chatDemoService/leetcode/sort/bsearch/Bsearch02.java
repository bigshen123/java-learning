package com.bigshen.chatDemoService.leetcode.sort.bsearch;

/**
 * @ClassName Bsearch02
 * @Description: TODO 二分查找变体
 * @Author BYJ
 * @Date 2020/10/9
 * @Version V1.0
 **/
public class Bsearch02 {

    /**
     * 查找第一个值等于给定值的元素
     * @param a
     * @param n
     * @param value
     * @return
     */
    public static int bsearch01(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid = low + ((high - low) >> 1);
            if (a[mid] >= value) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        if (low < n && a[low]==value) {
            return low;
        } else {
            return -1;
        }
    }


    public static int bsearch02(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid =  low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                if ((mid == 0) || (a[mid - 1] != value)) return mid;
                else high = mid - 1;
            }
        }
        return -1;
    }


    /**
     * 查找最后一个值等于给定值的元素
     * @param a
     * @param n
     * @param value
     * @return
     */
    public static int bsearch03(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid =  low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else if (a[mid] < value) {
                low = mid + 1;
            } else {
                if ((mid == n - 1) || (a[mid + 1] != value)) return mid;
                else low = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 查找第一个大于等于给定值的元素
     * @param a
     * @param n
     * @param value
     * @return
     */
    public static int bsearch04(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid =  low + ((high - low) >> 1);
            if (a[mid] >= value) {
                if ((mid == 0) || (a[mid - 1] < value)) return mid;
                else high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;
    }

    /**
     * 查找最后一个小于等于给定值的元素
     * @param a
     * @param n
     * @param value
     * @return
     */
    public static int bsearch7(int[] a, int n, int value) {
        int low = 0;
        int high = n - 1;
        while (low <= high) {
            int mid =  low + ((high - low) >> 1);
            if (a[mid] > value) {
                high = mid - 1;
            } else {
                if ((mid == n - 1) || (a[mid + 1] > value)) return mid;
                else low = mid + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        int a[]={1,3,6,9,12,45};
        //int i = bsearch01(a, 6, 9);
        int i = bsearch01(a, 6, 1);
        System.out.println(i);
    }

}
