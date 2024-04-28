package com.bigshen.learningDemo.javaSE.jni.myJni;

/**
 * @Author BYJ
 * @Date 2023/11/11 17:55
 * @Describe
 */
class MyJNI {
    native static int myTest(int[] dstArray, int[] srcArray);
}

public class Test {

    static {
        System.loadLibrary("MyJNI");
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        int[] dstArray = new int[2];
        int[] srcArray = {100, 600};

        System.out.println("The value is: " + MyJNI.myTest(dstArray, srcArray));
        System.out.println("The sum is: " + (dstArray[0] + dstArray[1]));
    }
}
