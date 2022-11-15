package com.bigshen.learningDemo.demo.test;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author BYJ
 * @Date 2021/3/14 10:10
 * @Describe
 */
@Slf4j
public class Test {
    private String myValue = "aaa";

    public Test(String s) {
        this.myValue = s;
    }

    public void printValue() {
        System.out.println(this.myValue);
    }

    static public void changeValue(String s) {
        s = "22222";
    }

    public static void main(String[] args) {
        try {
            String s = "1111";
            Test.changeValue(s);
            new Test(s).printValue();
            String a = "adssasdsaasddadas";
            int count = StringUtils.countMatches(a, 'a');
            System.out.println(count);

            new Thread(Test::test2).start();
            System.out.println(1111);
        } catch (Exception e) {
            System.out.println("父级异常捕获" + e);
        }
        System.out.println(22222);
    }

    private static void test2() {
        int i = 1 / 0;
    }
}
