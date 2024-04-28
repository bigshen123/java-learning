package com.bigshen.learningDemo.javaSE.split;

import org.apache.commons.lang3.StringUtils;

/**
 * @Description:
 * 有一串字符串String s = "ababab", 这个字符串可以看做由3个"ab"构成,
 * 即n=3, L = "ab", s = nL. 现在要求编写一段程序,
 * 使用单例模式,输入任意字符串s,输出nL.
 * 如输入: aaaaa 输出 5a ,输入: ababa 输出: 1ababa
 * @Author: byj
 * @Date: 2019/12/6 11:14*/


public class Tool {

    //创建一个对象
    private static Tool tool;

    //让构造函数为 private，这样该类就不会被实例化
    private Tool(){}

    //获取唯一可用对象
    private static Tool getTool() {
        if (tool == null) {
            tool = new Tool();
        }
        return tool;
    }

    //编写用来解题的方法
    private static String compress(String s){
        String result = "";
        if (StringUtils.isBlank(s)){
            result ="你输入为空!";
        }else if (s.contains(" ")){
            result ="请不要输入空格!";
        }else {
            int length = s.length();
            System.out.println("length: "+length);
            int count=0;
            for (int i= 1;i<=length;i++){
                String[] strings1 = s.split(s.substring(0,i));
                if (strings1.length==0){
                    System.out.println("切分到"+i);
                    count = i;
                    break;
                }
            }
            result = length/count + s.substring(0,count);
        }
        return result;
    }

    public static void main(String[] args) {
        String s = "adada";
        String result = "";
        Tool tool = Tool.getTool();
        result = tool.compress(s);
        System.out.println(result);
    }
}
