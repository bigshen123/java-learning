package com.bigshen.learningDemo.demo.ms;

import java.util.*;

/**
 * @Author BYJ
 * @Date 2023/3/12 10:56
 * @Describe 可以使用动态规划（DP）来解决。具体来说，我们可以定义一个数组 dp，其中 dp[i] 表示字符串 s 的前 i 个字符是否可以被拆分成 wordDict 中的单词。
 * 初始时，dp[0] 为 true，即空串可以被拆分成任意个单词。
 * <p>
 * 接下来，我们枚举字符串 s 的所有子串，看它是否能被拆分成 wordDict 中的单词，如果可以，则 dp[i] 置为 true。具体而言，我们可以枚举 s 的所有前缀，
 * 如果前缀可以被拆分成 wordDict 中的单词，并且剩余的后缀也可以被拆分成单词，则 s 也可以被拆分成单词。状态转移方程如下：
 * <p>
 * dp[i] = dp[j] && check(s[j:i-1])
 * <p>
 * 其中 check(s[j:i-1]) 表示 s 的子串 s[j:i-1] 是否可以被拆分成 wordDict 中的单词。
 * <p>
 * 最终，如果 dp[n] 为 true，则说明整个字符串 s 可以被拆分成 wordDict 中的单词。我们可以使用回溯算法来输出所有可能的拆分方案。具体而言，
 * 我们从后往前遍历 dp 数组，找到第一个 dp[i] 为 true 的位置 i，然后递归遍历 dp[0:i-1]，找到所有可能的拆分方案。当遍历到 dp[0] 时，
 * 说明已经找到了一种拆分方案，将其加入答案即可。
 */
class Test2 {


    public static List<String> wordBreak(String s, List<String> wordDict) {
        // 使用 set 存储 wordDict 中的单词，加快查找速度
        Set<String> wordSet = new HashSet<>(wordDict);

        // 使用 DP 判断字符串 s 是否可以被拆分成 wordDict 中的单词
        int n = s.length();
        boolean[] dp = new boolean[n + 1];
        dp[0] = true;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (dp[j] && wordSet.contains(s.substring(j, i))) {
                    dp[i] = true;
                    break;
                }
            }
        }

        // 回溯得到所有可能的拆分方案
        List<String> ans = new ArrayList<>();
        if (dp[n]) {
            dfs(s, wordSet, new StringBuilder(), n, ans, dp);
        }
        return ans;
    }

    private static void dfs(String s, Set<String> wordSet, StringBuilder path, int idx, List<String> ans, boolean[] dp) {
        if (idx == 0) {
            ans.add(path.substring(0, path.length() - 1));
            return;
        }

        for (int i = idx - 1; i >= 0; i--) {
            if (dp[i] && wordSet.contains(s.substring(i, idx))) {
                String word = s.substring(i, idx);
                path.insert(0, word).insert(0, " ");
                dfs(s, wordSet, path, i, ans, dp);
                path.delete(0, word.length() + 1);
            }
        }
    }

    public static void main(String[] args) {
        String s1 = "catsanddog";
        String[] wordDict1 = {"cat", "cats", "and", "sand", "dog"};
        List<String> wordDictList1 = Arrays.asList(wordDict1);
        List<String> strings = wordBreak(s1, wordDictList1);
        System.out.println(strings);

        String s2 = "pineapplepenapple";
        String[] wordDict2 = {"apple", "pen", "applepen", "pine", "pineapple"};
        List<String> wordDictList2 = Arrays.asList(wordDict2);
        List<String> strings2 = wordBreak(s2, wordDictList2);
        System.out.println(strings2);
    }
}

