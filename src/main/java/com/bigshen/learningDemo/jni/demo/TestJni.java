package com.bigshen.learningDemo.jni.demo;

/**
 * @Author BYJ
 * @Date 2023/11/11 17:59
 * @Describe
 */
public class TestJni {
    static {
        System.loadLibrary(("ctest"));
    }

    native static int myJNITest();

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        System.out.println("The answer is: " + myJNITest());
    }
}
