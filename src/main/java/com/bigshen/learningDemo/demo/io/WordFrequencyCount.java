package com.bigshen.learningDemo.demo.io;

import java.io.*;
import java.util.*;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/11/4 15:10
 */
public class WordFrequencyCount {
    public static void main(String[] args) {
        String string = "";
        //通过键值对的方式去分别存储字符串和出现的次数
        final Map<String, Integer> map = new HashMap<String, Integer>();
        try {
            //定义一个文件字节读取流，去读取磁盘中的文件
            FileInputStream fis = new FileInputStream("D:\\data.txt");
            //创建一个BufferedReader的缓冲流，将字符流对象放入进去，提高读取的效率
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String temp = "";
            try {
                //从BufferReader中读取下一行
                while ((temp = br.readLine()) != null) {
                    string = string + temp;//读取到的文件信息
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //创建一个spilt数组，分割字符串，来统计单词出现的次数
        String[] spilt = string.split(" ");
        for (int i = 0; i < spilt.length; i++) {

            //map.get(),通过键名来获取键值
            if (map.get(spilt[i]) == null) {
            //map.put()将键值存储在map集合中，如果存在，那么就覆盖该键值，如果不存在就新建一个。
                map.put(spilt[i], 1);
            } else {
                int frequency = map.get(spilt[i]);
                map.put(spilt[i], ++frequency);
            }
        }
        //利用TreeMap实现Comparator接口
        Comparator<Map.Entry<String, Integer>> valueComparator = new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();//降序排序 ;
            }
        };

        List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        // 排序
        Collections.sort(list, valueComparator);

        // 默认情况下，TreeMap对key进行升序排序 

        System.out.println("------------map按照value降序排序--------------------");

        for (Map.Entry<String, Integer> entry : list) {

            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }
}
