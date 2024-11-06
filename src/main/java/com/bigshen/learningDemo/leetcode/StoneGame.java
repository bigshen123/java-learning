package com.bigshen.learningDemo.leetcode;

/**
 * @author byj
 * @date 2024/10/24
 * @Description
 * 877. 石子游戏
 * Alice 和 Bob 用几堆石子在做游戏。一共有偶数堆石子，排成一行；每堆都有 正 整数颗石子，数目为 piles[i] 。
 *
 * 游戏以谁手中的石子最多来决出胜负。石子的 总数 是 奇数 ，所以没有平局。
 *
 * Alice 和 Bob 轮流进行，Alice 先开始 。 每回合，玩家从行的 开始 或 结束 处取走整堆石头。 这种情况一直持续到没有更多的石子堆为止，此时手中 石子最多 的玩家 获胜 。
 *
 * 假设 Alice 和 Bob 都发挥出最佳水平，当 Alice 赢得比赛时返回 true ，当 Bob 赢得比赛时返回 false 。
 *
 *
 * 示例 1：
 *
 * 输入：piles = [5,3,4,5]
 * 输出：true
 * 解释：
 * Alice 先开始，只能拿前 5 颗或后 5 颗石子 。
 * 假设他取了前 5 颗，这一行就变成了 [3,4,5] 。
 * 如果 Bob 拿走前 3 颗，那么剩下的是 [4,5]，Alice 拿走后 5 颗赢得 10 分。
 * 如果 Bob 拿走后 5 颗，那么剩下的是 [3,4]，Alice 拿走后 4 颗赢得 9 分。
 * 这表明，取前 5 颗石子对 Alice 来说是一个胜利的举动，所以返回 true 。
 * 示例 2：
 *
 * 输入：piles = [3,7,2,3]
 * 输出：true
 */
public class StoneGame  {

    public boolean stoneGame(int[] piles) {
        int n = piles.length;
        int[][] dp = new int[n][n];

        // 初始化dp[i][i]，当只剩一堆石子时，Alice只能拿那一堆
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }

        // 从小区间开始计算，逐步扩大区间
        for (int length = 2; length <= n; length++) {
            for (int i = 0; i + length - 1 < n; i++) {
                int j = i + length - 1;
                dp[i][j] = Math.max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1]);
            }
        }

        // 最终结果在dp[0][n-1]，如果dp[0][n-1] > 0，Alice赢
        return dp[0][n-1] > 0;
    }

    public static void main(String[] args) {
        StoneGame game = new StoneGame();
        int[] piles = {5,6, 3, 4, 5,4};
        System.out.println(game.stoneGame(piles)); // 输出 true
    }
}
