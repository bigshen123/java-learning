package com.bigshen.learningDemo.demo.test;

import org.apache.commons.lang3.StringUtils;

/**
 * @Author BYJ
 * @Date 2021/3/14 10:10
 * @Describe
 */
public class Test {
    private String myValue="aaa";

    public Test(String s){
        this.myValue=s;
    }

    public void printValue(){
        System.out.println(this.myValue);
    }

    static public void changeValue(String s){
        s="22222";
    }

    public static void main(String[] args) {
        String s="1111";
        Test.changeValue(s);
        new Test(s).printValue();
        String a = "adssasdsaasddadas";
        int count = StringUtils.countMatches(a, 'a');
        System.out.println(count);
    }
}
