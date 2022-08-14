package com.bigshen.learningDemo.demo.processor;

import java.util.Arrays;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:57
 */
public class Splitter extends Processor {
    String process(Object input){
        return Arrays.toString(((String) input).split(" "));
    }
}
