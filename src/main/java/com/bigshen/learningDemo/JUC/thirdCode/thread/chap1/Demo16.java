package com.bigshen.learningDemo.JUC.thirdCode.thread.chap1;

public class Demo16 {
    public static void main(String[] args) {
        Thread.currentThread().interrupt();
        System.out.println("�Ƿ�ֹͣ1��" + Thread.interrupted());
        System.out.println("�Ƿ�ֹͣ2��" + Thread.interrupted());
    }
}
