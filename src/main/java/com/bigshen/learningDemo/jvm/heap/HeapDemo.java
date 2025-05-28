package com.bigshen.learningDemo.jvm.heap;

import java.util.Random;

/**
 * @author byj
 * @date 2025/5/28
 * @Description
 */
public class HeapDemo {
    public static void main(String[] args) {
        long maxMemory = Runtime.getRuntime().maxMemory();
        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println(maxMemory/(double)1024/1024 + "MB");
        System.out.println(totalMemory/(double)1024/1024 + "MB");

        while (true){
            // 每次创建一个新字符串对象，没有持久引用，会很快变成垃圾
            String data = new String(new byte[1024]); // 1KB
        }
    }
}
