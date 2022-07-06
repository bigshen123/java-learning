package com.bigshen.chatDemoService.demo.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:正则表达式截取
 * @Author: BIGSHEN
 * @Date: 2019/12/7 8:34
 */
public class RegExpTest {
    public static void main(String[] args) {
        String string="北京市朝阳区西城区)(海淀区)";
        Pattern pattern=Pattern.compile(".*?(?=\\()");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()){
            System.out.println(matcher.group());
        }
    }
}
