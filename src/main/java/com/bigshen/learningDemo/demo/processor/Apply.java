package com.bigshen.learningDemo.demo.processor;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/21 15:58
 */
public class Apply {

    public static String string = "Disagreement with beliefs is by definition incorrect";

    public static void main(String[] args) {
        process(new Upcase(),string);
        process(new DownCase(),string);
        process(new Splitter(),string);
    }
    private static void process(Processor p, Object s){
        System.out.println("Using Process"+p.name());
        System.out.println(p.process(s));
    }
}
