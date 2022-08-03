package com.bigshen.learningDemo.leetcode.arrays;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName SubarraysDivByK
 * @Description: TODO
 * 给定一个整数数组 A，返回其中元素之和可被 K 整除的（连续、非空）子数组的数目。
 *
 * 示例：
 * 输入：A = [4,5,0,-2,-3,1], K = 5
 * 输出：7
 * 解释：
 * 有 7 个子数组满足其元素之和可被 K = 5 整除：
 * [4, 5, 0, -2, -3, 1], [5], [5, 0], [5, 0, -2, -3], [0], [0, -2, -3], [-2, -3]
 * 提示：
 *
 * 1 <= A.length <= 30000
 * -10000 <= A[i] <= 10000
 * 2 <= K <= 10000
 *
 * @Author BYJ
 * @Date 2020/5/27
 * @Version V1.0
 **/
public class SubarraysDivByK {
    public static int subarraysDivByK(int[] A, int K) {
        // 余数统计，下标为余数，值为余数个数
        int[] modCount = new int[K];
        // 余数为0的算特列（开始就算一个组合）， 余数为其它的，第二个开始算一个组合
        modCount[0] = 1;
        int sum = 0;
        int count = 0;
        for (int anArr : A) {
            sum += anArr;
            int mod = (sum % K + K) % K;
            count += modCount[mod]++;
        }
        return count;
    }

    public static int subarraysDivByK2(int[] A, int K) {
        Map<Integer, Integer> map = new HashMap<>();
        int sum = 0;
        int result = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i];
            if (sum % K < 0) {
                map.put(sum % K + K, map.containsKey(sum % K + K) ? map.get(sum % K + K) + 1 : 1);
            } else {
                map.put(sum % K, map.containsKey(sum % K) ? map.get(sum % K) + 1 : 1);
            }
        }
        for (int i = 0; i < K; i++) {
            if (map.containsKey(i)) {
                if (i == 0) {
                    result += map.get(i) * (map.get(i) + 1) / 2;
                } else {
                    result += map.get(i) * (map.get(i) - 1) / 2;
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int[] arr={4,5,0,-2,-3,1};
        System.out.println(subarraysDivByK(arr,5));
    }
}
