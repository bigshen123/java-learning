package com.bigshen.learningDemo.demo.jna;

import com.sun.jna.Library;
import com.sun.jna.Native;


/**
 * @Author BYJ
 * @Date 2024/1/22 22:01
 * @Describe 定义DLL库接口
 */
public interface AddLibrary extends Library {
    AddLibrary INSTANCE = Native.loadLibrary("add", AddLibrary.class);

    /**
     声明DLL中的函数
     */
    int add(int a, int b);
}
