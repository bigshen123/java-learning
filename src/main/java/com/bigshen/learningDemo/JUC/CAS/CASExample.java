package com.bigshen.learningDemo.JUC.CAS;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author BYJ
 * @Date 2024/10/29 19:23
 * @Describe
 */
public class CASExample {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(0);

        // CAS example: incrementing the value
        int expectedValue = atomicInteger.get();
        int newValue = expectedValue + 1;

        // CAS operation
        boolean success = atomicInteger.compareAndSet(expectedValue, newValue);
        System.out.println("CAS operation success: " + success);
        System.out.println("Current value: " + atomicInteger.get());
    }
}
