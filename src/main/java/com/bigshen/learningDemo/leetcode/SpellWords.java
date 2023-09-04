package com.bigshen.learningDemo.leetcode;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author BYJ
 * @Date 2023/3/22 20:37
 * @Describe 给你一份『词汇表』（字符串数组） words 和一张『字母表』（字符串） chars。
 * <p>
 * 假如你可以用 chars 中的『字母』（字符）拼写出 words 中的某个『单词』（字符串），那么我们就认为你掌握了这个单词。
 * <p>
 * 注意：每次拼写（指拼写词汇表中的一个单词）时，chars 中的每个字母都只能用一次。
 * <p>
 * 返回词汇表 words 中你掌握的所有单词的 长度之和。
 * <p>
 * eg：输入：words = ["cat","bt","hat","tree"], chars = "atach"
 * 输出：6
 * 解释：
 * 可以形成字符串 "cat" 和 "hat"，所以答案是 3 + 3 = 6。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode.cn/problems/find-words-that-can-be-formed-by-characters
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class SpellWords {

    public static void main(String[] args) {
        String[] words = new String[]{"hello", "world", "leetcode"};
        String chars = "welldonehoneyr";
        int wordsLength = spellWords(words, chars);
        System.out.println(wordsLength);

        String[] words2 = new String[]{"cat", "bt", "hat", "tree"};
        String chars2 = "atach";
        System.out.println(spellWords(words2, chars2));

        String[] words3 = new String[]{"dyiclysmffuhibgfvapygkorkqllqlvokosagyelotobicwcmebnpznjbirzrzsrtzjxhsfpiwyfhzyonmuabtlwin", "ndqeyhhcquplmznwslewjzuyfgklssvkqxmqjpwhrshycmvrb", "ulrrbpspyudncdlbkxkrqpivfftrggemkpyjl", "boygirdlggnh", "xmqohbyqwagkjzpyawsydmdaattthmuvjbzwpyopyafphx", "nulvimegcsiwvhwuiyednoxpugfeimnnyeoczuzxgxbqjvegcxeqnjbwnbvowastqhojepisusvsidhqmszbrnynkyop", "hiefuovybkpgzygprmndrkyspoiyapdwkxebgsmodhzpx", "juldqdzeskpffaoqcyyxiqqowsalqumddcufhouhrskozhlmobiwzxnhdkidr", "lnnvsdcrvzfmrvurucrzlfyigcycffpiuoo", "oxgaskztzroxuntiwlfyufddl", "tfspedteabxatkaypitjfkhkkigdwdkctqbczcugripkgcyfezpuklfqfcsccboarbfbjfrkxp", "qnagrpfzlyrouolqquytwnwnsqnmuzphne", "eeilfdaookieawrrbvtnqfzcricvhpiv", "sisvsjzyrbdsjcwwygdnxcjhzhsxhpceqz", "yhouqhjevqxtecomahbwoptzlkyvjexhzcbccusbjjdgcfzlkoqwiwue", "hwxxighzvceaplsycajkhynkhzkwkouszwaiuzqcleyflqrxgjsvlegvupzqijbornbfwpefhxekgpuvgiyeudhncv", "cpwcjwgbcquirnsazumgjjcltitmeyfaudbnbqhflvecjsupjmgwfbjo", "teyygdmmyadppuopvqdodaczob", "qaeowuwqsqffvibrtxnjnzvzuuonrkwpysyxvkijemmpdmtnqxwekbpfzs", "qqxpxpmemkldghbmbyxpkwgkaykaerhmwwjonrhcsubchs"};
        String chars3 = "usdruypficfbpfbivlrhutcgvyjenlxzeovdyjtgvvfdjzcmikjraspdfp";
        System.out.println(spellWords(words3, chars3));

    }

    /**
     * 拼写单词
     *
     * @param words 单词集合
     * @param chars 指定单词
     * @return 可以拼写的单词长度之和
     */
    private static int spellWords(String[] words, String chars) {
        int ans = 0;
        Map<Character, Integer> charCount = new HashMap<>(Maps.newHashMapWithExpectedSize(50));
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            charCount.put(c, charCount.getOrDefault(c, 0) + 1);
        }
        for (String word : words) {
            Map<Character, Integer> wordCount = new HashMap<>(Maps.newHashMapWithExpectedSize(50));
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                wordCount.put(c, wordCount.getOrDefault(c, 0) + 1);
            }
            boolean canFormWord = true;
            for (Map.Entry<Character, Integer> entry : wordCount.entrySet()) {
                char c = entry.getKey();
                int count = entry.getValue();
                if (!charCount.containsKey(c) || charCount.get(c) < count) {
                    canFormWord = false;
                    break;
                }
            }
            if (canFormWord) {
                ans += word.length();
            }
        }
        return ans;
    }
}
