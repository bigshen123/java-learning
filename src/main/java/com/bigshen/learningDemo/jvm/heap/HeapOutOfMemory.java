package com.bigshen.learningDemo.jvm.heap;

import java.util.ArrayList;

/**
 * @Description: 堆内存溢出Demo
 * @Author: BIGSHEN
 * @Date: 2019/12/21 11:27
 */
public class HeapOutOfMemory {
    public static void main(String[] args) {
        ArrayList list=new ArrayList();
        while (true){
           list.add(new HeapOutOfMemory());
        }
    }
}
