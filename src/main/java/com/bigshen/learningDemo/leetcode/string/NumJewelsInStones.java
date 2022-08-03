package com.bigshen.learningDemo.leetcode.string;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName NumJewelsInStones
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/5/24
 * @Version V1.0
 **/
public class NumJewelsInStones {
    public static int numJewelsInStones(String J, String S) {
        int count=0;
        char[] pearlChars = J.toCharArray();
        char[] strogeChars = S.toCharArray();
        for (char c : pearlChars) {
            for (char strogeChar : strogeChars) {
                if (c==strogeChar){
                    count++;
                }
            }
        }
        return count;
    }
    public static int numJewelsInStones2(String J, String S) {
        if (S == null || S.isEmpty()) return 0;
        if (J == null || J.isEmpty()) return 0;

        byte[] arr = new byte[58];
        int count = 0;
        for (char ch : J.toCharArray()) {
            arr[ch - 65] = 1;
        }
        for (char ch : S.toCharArray()) {
            if(arr[ch -65] == 1) {
                count++;
            };
        }
        return count;
    }
    public static int numJewelsInStones3(String J, String S) {
        String a = "[^"+J+']';
        S = S.replaceAll(a, "");
        return S.length();
    }
    public static int numJewelsInStones4(String J, String S) {
        Set<Character> Jset = new HashSet();
        for (char j: J.toCharArray())
            Jset.add(j);

        int ans = 0;
        for (char s: S.toCharArray())
            if (Jset.contains(s))
                ans++;
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(numJewelsInStones("aA","aAAbbbb"));
    }
}
