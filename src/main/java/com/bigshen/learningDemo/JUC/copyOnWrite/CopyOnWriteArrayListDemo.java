package com.bigshen.learningDemo.JUC.copyOnWrite;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author BYJ
 * @Date 2024/10/30 19:59
 * @Describe
 */
public class CopyOnWriteArrayListDemo {

    public static void main(String[] args) {
        CopyOnWriteArrayList<Integer> cowal = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 10; i++) {
            cowal.add(i);
        }
        PutThread p1 = new PutThread(cowal);
        p1.start();
        Iterator<Integer> iterator = cowal.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        iterator = cowal.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
    }

    static class PutThread extends Thread {
        private CopyOnWriteArrayList<Integer> cowal;

        public PutThread(CopyOnWriteArrayList<Integer> cowal) {
            this.cowal = cowal;
        }

        @Override
        public void run() {
            try {
                for (int i = 100; i < 110; i++) {
                    cowal.add(i);
                    Thread.sleep(50);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
