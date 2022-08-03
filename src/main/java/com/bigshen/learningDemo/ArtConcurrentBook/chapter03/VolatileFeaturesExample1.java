package com.bigshen.learningDemo.ArtConcurrentBook.chapter03;

class VolatileFeaturesExample1 {
    long vl = 0L; // 64λ��long����ͨ����

    public synchronized void set(long l) {//�Ե�������ͨ������д��ͬһ����ͬ��
        vl = l;
    }

    public void getAndIncrement() { //��ͨ��������
        long temp = get(); //������ͬ���Ķ�����
        temp += 1L; //��ͨд����
        set(temp); //������ͬ����д����
    }

    public synchronized long get() { //�Ե�������ͨ�����Ķ���ͬһ����ͬ��
        return vl;
    }
}
