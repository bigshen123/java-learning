package com.bigshen.learningDemo.ArtConcurrentBook.chapter03;

class SynchronizedExample {
    int     a    = 0;
    boolean flag = false;

    public synchronized void writer() { //��ȡ��
        a = 1;
        flag = true;
    } //�ͷ���

    public synchronized void reader() { //��ȡ��
        if (flag) {
            int i = a;
            //����
        } //�ͷ���
    }
}
