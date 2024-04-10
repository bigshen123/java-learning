package com.bigshen.learningDemo.JUC.thirdCode.ArtConcurrentBook.chapter03;

class VolatileBarrierExample {
    int          a;
    volatile int v1 = 1;
    volatile int v2 = 2;

    void readAndWrite() {
        int i = v1; //��һ��volatile��
        int j = v2; // �ڶ���volatile��
        a = i + j; //��ͨд
        v1 = i + 1; // ��һ��volatileд
        v2 = j * 2; //�ڶ��� volatileд
    }

    //��                 //��������
}
