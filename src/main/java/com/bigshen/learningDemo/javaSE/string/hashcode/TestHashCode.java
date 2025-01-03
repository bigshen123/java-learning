package com.bigshen.learningDemo.javaSE.string.hashcode;

import org.openjdk.jol.vm.VM;

/**
 * @author byj
 * @date 2024/4/18
 * @Description GC前后的hashCode值测试
 */
public class TestHashCode {
    public static void main(String[] args) {
        Object obj = new Object();
        long address = VM.current().addressOf(obj);
        long hashCode = obj.hashCode();
        System.out.println("before GC : The memory address is " + address);
        System.out.println("before GC : The hash code is " + hashCode);

        new Object();
        new Object();
        new Object();

        System.gc();

        long afterAddress = VM.current().addressOf(obj);
        long afterHashCode = obj.hashCode();
        System.out.println("after GC : The memory address is " + afterAddress);
        System.out.println("after GC : The hash code is " + afterHashCode);
        System.out.println("---------------------");

        System.out.println("memory address = " + (address == afterAddress));
        System.out.println("hash code = " + (hashCode == afterHashCode));
    }
}
