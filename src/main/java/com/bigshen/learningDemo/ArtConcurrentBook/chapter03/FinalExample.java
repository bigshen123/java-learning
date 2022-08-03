package com.bigshen.learningDemo.ArtConcurrentBook.chapter03;

public class FinalExample {
    int                 i;  //��ͨ����
    final int           j;  //final����
    static FinalExample obj;

    public FinalExample() { //���캯��
        i = 1; //д��ͨ��
        j = 2; //дfinal��
    }

    public static void writer() { //д�߳�Aִ��
        obj = new FinalExample();
    }

    public static void reader() { //���߳�Bִ��
        FinalExample object = obj; //����������
        int a = object.i; //����ͨ��
        int b = object.j; //��final��
    }
}