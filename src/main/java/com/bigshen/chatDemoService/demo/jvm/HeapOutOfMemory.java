package com.bigshen.chatDemoService.demo.jvm;

import java.util.ArrayList;

/**
 * @Description:
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
