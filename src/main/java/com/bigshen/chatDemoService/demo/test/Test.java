package com.bigshen.chatDemoService.demo.test;

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
    }
}
