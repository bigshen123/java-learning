package com.bigshen.learningDemo.concurrent.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/1 17:28
 */
public class CountDownLatchTest {
    private static CountDownLatch countDownLatch = new CountDownLatch(5);

    /**
     * Boss�̣߳��ȴ�Ա�����￪��
     */
    static class BossThread extends Thread {
        @Override
        public void run() {
            System.out.println("Boss�ڻ����ҵȴ����ܹ���" + countDownLatch.getCount() + "���˿���...");
            try {
                //Boss�ȴ�
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("�����˶��Ѿ������ˣ������...");
        }
    }
    /**
     Ա�����������
     */
    static class EmpleoyeeThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "�����������....");
            //Ա����������� count - 1
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) {
        //Boss�߳�����
        new BossThread().start();

        for (int i = 0; i < countDownLatch.getCount(); i++) {
            new EmpleoyeeThread().start();
        }
    }
}
