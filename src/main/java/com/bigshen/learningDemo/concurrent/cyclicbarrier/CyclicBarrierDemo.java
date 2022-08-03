package com.bigshen.learningDemo.concurrent.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;

/**
 * @Description:ͬ������demo���ռ�7�������ٻ�����
 * @Author: BIGSHEN
 * @Date: 2019/12/11 17:38
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier=new CyclicBarrier(7,()-> System.out.println("----�ٻ�����~~~"));
        for (int i = 1; i <= 7; i++) {
            final int tep=i;
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+"\t �ռ����ڣ�"+tep+"��������");
                try {
                    cyclicBarrier.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },String.valueOf(i)).start();
        }
    }
}
