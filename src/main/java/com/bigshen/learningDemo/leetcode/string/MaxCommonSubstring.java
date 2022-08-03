package com.bigshen.learningDemo.leetcode.string;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @ClassName MaxcommonSubstring
 * @Description:TODO 最大公共子串 算法
 * 给定两个字符串str1和str2,输出两个字符串的最长公共子串，如果最长公共子串为空，输出-1。
 * 比如："ab123cd"，"a123456c"，
 * 返回："123"，
 * 备注：1≤|str1|,|str2|≤5000
 * @Author: byj
 * @Date: 2020/12/10
 */
public class MaxCommonSubstring {

    /**
     * 双重for循环
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String maxCommonSub(String str1, String str2) {
        //参数校验
        if (str1 == null || "".equals(str1) || str2 == null || "".equals(str2)) {
            return "-1";
        }
        //先将两者转为char数组
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        //maxLength是出现的最大公共子串长度
        int maxLength = 0;
        int maxEndIndexInArray1 = 0;
        for (int i = 0; i < chars1.length; i++) {
            for (int j = 0; j < chars2.length; j++) {
                if (chars1[i] == chars2[j]) {
                    int tempIndex1 = i;
                    int tempIndex2 = j;
                    int tempMax = 0;
                    while (tempIndex1 < chars1.length && tempIndex2 < chars2.length && chars1[tempIndex1] == chars2[tempIndex2]) {
                        tempMax++;
                        tempIndex1++;
                        tempIndex2++;
                    }
                    if (tempMax > maxLength) {
                        maxLength = tempMax;
                        //因为后面substring方法是包头不包尾，这里不用减1
                        maxEndIndexInArray1 = tempIndex1;
                    }
                }
            }
        }
        if (maxLength == 0) {
            return "-1";
        } else {
            return str1.substring(maxEndIndexInArray1 - maxLength, maxEndIndexInArray1);
        }
    }

    /**
     * 对于str1中每个元素，都循环一次str2中所有元素有点浪费。
     * 可以用str2的元素集合构建一个map，key是元素值，value是元素的index。
     * 考虑到可能多个index同样的值，value可以设置为多个index用逗号分隔的字符串。
     * 这样以str1元素为驱动元素的话，只需要在map中找是否有对应的元素，有的话取出他们在str2中的索引列表。
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String maxCommonSub2(String str1, String str2) {
        //参数校验
        if (str1 == null || "".equals(str1) || str2 == null || "".equals(str2)) {
            return "-1";
        }
        //先将两者转为char数组
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        //构建char2Map，即char --> index 如果相同char的index有多个则为index1
        //index2，构建这个是为了再arr1遍历是O（1）查找char再arr2的index列表
        Map<Character, String> char2Map = new HashMap<>(chars1.length);
        for (int i = 0; i < chars2.length; i++) {
            if (char2Map.containsKey(chars2[i])) {
                char2Map.put(chars2[i], char2Map.get(chars2[i]) + "," + i);
            } else {
                char2Map.put(chars2[i], i + "");
            }
        }
        //max是出现的最大公共长度，maxIndex是最大公共长度在char1的起始处索引 "ab123cd", "a123456c"
        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < chars1.length; i++) {
            if (char2Map.containsKey(chars1[i])) {
                //说明2中有这个字符,那么需要计算
                String index2Arr = char2Map.get(chars1[i]);
                //转换出arr2中有这个char的index列表
                List<Integer> indexList = Stream.of(index2Arr.split(",")).map(Integer::parseInt).collect(Collectors.toList());
                for (Integer index2 : indexList) {
                    //tempIndex1用于标记在arr1的遍历序号
                    //tempCommonLength用于标记从index2起相同长度，初始是0
                    int tempIndex1 = i;
                    int tempCommonLength = 0;
                    while (index2 < chars2.length && tempIndex1 < chars1.length && chars2[index2] == chars1[tempIndex1]) {
                        tempCommonLength++;
                        tempIndex1++;
                        index2++;
                    }
                    if (tempCommonLength > max) {
                        max = tempCommonLength;
                        maxIndex = i;
                    }
                }
            }
        }
        if (max == 0) {
            return "-1";
        } else {
            return str1.substring(maxIndex, maxIndex + max);
        }
    }

    /**
     * 可以考虑用一个二维数组dp [m+1] [n+1]来保存 str1[m] 与 str2[n] 是否相同的信息？
     * 如果 str1[m]!=str2[n]，dp[m+1] [n+1]=0 标识他们不相等。
     * 如果str1[m]==str2[n]，dp[m+1] [n+1]则不为0，标识他们相等。
     * 同时这个点值存放的是dp[m+1] [n+1]点即str1[m]与str2[n]点已经达到的公共子串长度。
     * <p>
     * 如果str1[m]==str2[n]，dp[m+1] [n+1]=dp[m] [n]+1，而对于str1中m为0，str2中n为0时，如果用dp[0] [0]来保存，则没有dp[-1] [-1]的元素（数组元素下标最小为0）。
     * 因此都是用dp[m+1] [n+1]来标识str1[m]，str2[n]是否相等信息。
     *
     * @param str1
     * @param str2
     * @return
     */
    public static String maxCommonSub3(String str1, String str2) {
        //参数校验
        if (str1 == null || "".equals(str1) || str2 == null || "".equals(str2)) {
            return "-1";
        }
        //先将两者转为char数组
        char[] chars1 = str1.toCharArray();
        char[] chars2 = str2.toCharArray();
        int size1 = chars1.length;
        int size2 = chars2.length;
        //先将第一列和第二列置为0
        int[][] dp = new int[size1 + 1][size2 + 1];
        for (int i = 0; i < size1; i++) {
            dp[i][0] = 0;
            dp[0][i] = 0;
        }
        //maxLength保存数组中出现过的最大字符串
        //maxIndexInChar1标识最大字符串的末尾字符再str1中的index
        int maxLength = 0;
        int maxIndexInChar1 = 0;
        for (int i = 0; i < size1; i++) {
            for (int j = 0; j < size2; j++) {
                if (chars1[i] != chars2[j]) {
                    dp[i + 1][j + 1] = 0;
                } else {
                    dp[i + 1][j + 1] = dp[i][j] + 1;
                    if (dp[i + 1][j + 1] > maxLength) {
                        maxLength = dp[i + 1][j + 1];
                        maxIndexInChar1 = i;
                    }
                }
            }
        }
        if (maxIndexInChar1 == 0) {
            return "-1";
        } else {
            //根据长度和再字符串str1中下标截出最大公共子串
            return str1.substring(maxIndexInChar1 + 1 - maxLength, maxIndexInChar1 + 1);
        }
    }

    public static void main(String[] args) {
        //String s = maxCommonSub("ab123cd", "a123456c");
        //String s = maxCommonSub2("ab123cd", "a123456c");
        String s = maxCommonSub3("ab123cd", "a123456c");
        System.out.println(s);
    }

}
