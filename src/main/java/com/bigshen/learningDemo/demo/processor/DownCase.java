package com.bigshen.learningDemo.demo.processor;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:56
 */
public class DownCase extends Processor {
    String process(Object input){
        return ((String) input).toLowerCase();
    }
}
