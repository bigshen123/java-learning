package com.bigshen.learningDemo.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author byj
 * @date 2023/3/17
 * @deprecated 这个算法的基本思路是将所有的区间按照起点进行排序，然后遍历每个区间，如果当前区间与前一个区间有重叠，则将它们合并，
 * 否则将当前区间添加到结果集中。最后将List转换成数组返回即可。
 */
public class MergeInterval {
    public static int[][] merge(int[][] intervals) {
        // 将所有区间按照起点排序
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();

        for (int[] interval : intervals) {
            // 如果当前区间与前一个区间有重叠，则将它们合并
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(interval);
            } else {
                merged.get(merged.size() - 1)[1] = Math.max(merged.get(merged.size() - 1)[1], interval[1]);
            }
        }

        // 将List转换成数组
        int[][] result = new int[merged.size()][2];
        for (int i = 0; i < merged.size(); i++) {
            result[i] = merged.get(i);
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] intervals = {{2, 6}, {1, 3}, {8, 10}, {15, 18}};
        int[][] merge = merge(intervals);
        System.out.println(Arrays.deepToString(merge));
    }
}
