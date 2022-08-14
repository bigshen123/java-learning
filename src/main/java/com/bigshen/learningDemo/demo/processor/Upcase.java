package com.bigshen.learningDemo.demo.processor;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:54
 */
public class Upcase extends Processor {
    String process(Object input){
        return ((String) input).toUpperCase();
    }
}
