package com.bigshen.learningDemo.leetcode;

import java.util.PriorityQueue;

/**
 * @Author BYJ
 * @Date 2023/7/3 21:35
 * @Describe 无序数列中求第k大的数(维护最小堆，然后依次遍历，与堆顶比较)
 */
public class KthLargestElement {

    public static int findKthLargest(int[] nums, int k) {
        // 创建一个最小堆，并将数列的前k个元素插入到堆中
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        // 对于剩余的数列中的每个元素，依次执行以下操作：
        //如果当前元素大于堆顶元素（堆中最小值），则将堆顶元素删除，并将当前元素插入到堆中。否则，忽略当前元素。
        for (int i = 0; i < k; i++) {
            minHeap.offer(nums[i]);
        }

        // 当遍历完整个数列后，堆中的堆顶元素即为第k大的数
        for (int i = k; i < nums.length; i++) {
            if (nums[i] > minHeap.peek()) {
                minHeap.poll();
                minHeap.offer(nums[i]);
            }
        }

        return minHeap.peek();
    }

    public static void main(String[] args) {
        int[] nums = {3, 1, 8, 4, 5, 2, 7, 6};
        int k = 4;
        int result = findKthLargest(nums, k);
        System.out.println("第" + k + "大的数是：" + result);
    }
}
