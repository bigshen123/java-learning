package com.bigshen.learningDemo.javaSE.string.split;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * Java求字符串中出现次数最多的字符，
 * 如String Str = "aaabbcddddee";那么输出：d 4 ;若String Str = "aaabbcddddeexxxxxx";那么输出：x 6
 * @Author: byj
 * @Date: 2019/12/6 10:50
 */
public class Maxkey {
    public static void main(String[] args) {
        String str="aabbbbccddeeeeeeeeee";
        char[] chars = str.toCharArray();
        //统计
        Map<Character,Integer> mapFunction= mapFunction(chars);
        //筛出出现最多字符
        Map<Character, Integer> maxChar = findMaxChar(mapFunction);
        System.out.println(maxChar);
    }

    private static Map<Character, Integer> findMaxChar(Map<Character, Integer> mapFunction) {
        Map<Character,Integer> map=new HashMap<>();
        int max=Integer.MIN_VALUE;
        char maxChar = 0;
        for (Map.Entry<Character, Integer> entry : mapFunction.entrySet()) {
            if (entry.getValue()>max){
                max=entry.getValue();
                maxChar=entry.getKey();
            }
        }
        map.put(maxChar,max);
        return map;
    }

    private static Map<Character, Integer> mapFunction(char[] chars) {
        Map<Character,Integer> map = new HashMap<Character,Integer>();
        if (chars!=null && chars.length!=0){
            for (char aChar : chars) {
                if (map.get(aChar) != null) {
                    map.put(aChar, map.get(aChar) + 1);
                } else {
                    map.put(aChar, 1);
                }
            }
        }
        return map;
    }
}
