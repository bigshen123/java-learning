package com.bigshen.learningDemo.javaSE.string.split;

import java.util.*;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/11/4 15:22
 */
public class WordCut {
    public static void main(String[] args){
        String wordsinput =  "今天 天气 不错 今天 太阳 也 不错";
        HashMap<String, Integer> wordMap = new  HashMap<String, Integer>();
        String[] words = wordsinput.split(" ");
        for(int i = 0; i < words.length ; i++){
            if(!wordMap.containsKey(words[i])){
                wordMap.put(words[i], 1);
            }else{
                int val = wordMap.get(words[i]);
                wordMap.put(words[i],val + 1);
            }
        }

        List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(
                wordMap.entrySet());
        Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return ( o2.getValue()-o1.getValue());
            }
        });
        for (int i = 0; i < infoIds.size(); i++) {
            Map.Entry<String,Integer> ent=infoIds.get(i);
            System.out.println(ent.getKey()+"="+ent.getValue());

        }
    }
}
