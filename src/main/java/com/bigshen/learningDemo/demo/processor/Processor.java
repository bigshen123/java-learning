package com.bigshen.learningDemo.demo.processor;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:53
 */
public class Processor {
    public String name(){
        return getClass().getSimpleName();
    }
    Object process(Object input){
        return input;
    }
}
