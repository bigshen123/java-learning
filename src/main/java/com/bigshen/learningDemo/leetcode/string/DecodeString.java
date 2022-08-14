package com.bigshen.learningDemo.leetcode.string;

import java.util.Stack;

/**
 * @ClassName DecodeString
 * @Description: TODO
 * 给定一个经过编码的字符串，返回它解码后的字符串。
 * 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。注意 k 保证为正整数。
 * 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，且输入的方括号总是符合格式要求的。
 * 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，例如不会出现像 3a 或 2[4] 的输入。
 * 示例:
 *
 * s = "3[a]2[bc]", 返回 "aaabcbc".
 * s = "3[a2[c]]", 返回 "accaccacc".
 * s = "2[abc]3[cd]ef", 返回 "abcabccdcdcdef".
 * @Author BYJ
 * @Date 2020/5/28
 * @Version V1.0
 **/
public class DecodeString {
    public static String decodeString(String s) {
        if (s.length() == 0) return "";
        Stack<Character> stack = new Stack<>();
        char[] characters = s.toCharArray();
        int i = 0;
        stack.add(characters[i]);
        i++;
        while (i < s.length())
        {
            if(characters[i] == ']')
            {
                StringBuilder tempBuilder = new StringBuilder();
                char c = stack.pop();
                while (c != '[')
                {
                    tempBuilder.append(c);
                    c = stack.pop();
                }
                char[] tempArray = tempBuilder.toString().toCharArray();
                StringBuilder tempNumber = new StringBuilder();
                while (!stack.isEmpty() && Character.isDigit(stack.peek()))
                {
                    tempNumber.append(stack.pop());
                }
                // 找出数字
                int t = Integer.parseInt(tempNumber.reverse().toString());
                while (t != 0)
                {
                    for (int k = tempArray.length - 1; k >= 0; k--)
                    {
                        stack.add(tempArray[k]);
                    }
                    t--;
                }
            }
            else
            {
                stack.add(characters[i]);
            }
            i++;
        }
        StringBuilder stringBUilder = new StringBuilder();
        while (!stack.isEmpty())
        {
            stringBUilder.append(stack.pop());
        }
        return stringBUilder.reverse().toString();
    }
    public static void main(String[] args) {
        System.out.println(decodeString("3[a2[c]]3[cd]"));
    }
}
