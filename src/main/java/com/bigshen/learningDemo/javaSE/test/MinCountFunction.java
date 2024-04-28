package com.bigshen.learningDemo.javaSE.test;

/**
 * @Description:
 * 自己设计一个泛型的获取数组最小值的函数.并且这个方法只能接受Number的子类并且实现了Comparable接口。
 * @Author: BIGSHEN
 * @Date: 2019/12/26 14:47
 */
public class MinCountFunction {
    private static <T extends Number & Comparable<? super T>> T min(T[] values){
        if (values==null || values.length==0) {
            return null;
        }
        T min=values[0];
        for (int i = 1; i < values.length; i++) {
            if (min.compareTo(values[i])>0) {
                min=values[i];
            }
        }
        return min;
    }

    public static void main(String[] args) {
        int minInteger = min(new Integer[]{1, 2, 3});//result:1
        System.out.println(minInteger);
        double minDouble = min(new Double[]{1.2, 2.2, -1d});//result:-1d
        System.out.println(minDouble);
        //String typeError = min(new String[]{"1","3"});//报错
    }
}
