package com.bigshen.learningDemo.demo.proxy;

/**
 * @ClassName AOPTest
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/6/25
 * @Version V1.0
 **/
public class AOPTest {
    public void begin() {
        System.out.println("开始事务");
    }
    public void close() {
        System.out.println("关闭事务");
    }
}
