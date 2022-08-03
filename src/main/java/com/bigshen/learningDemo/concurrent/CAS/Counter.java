package com.bigshen.learningDemo.concurrent.CAS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @ClassName Counter
 * @Description: TODO  CAS 保证线程安全测试demo
 * @Author BYJ
 * @Date 2020/5/24
 * @Version V1.0
 **/
public class Counter {
    private AtomicInteger atomicInteger=new AtomicInteger(0);
    private int i=0;

    public static void main(String[] args) {
        final Counter cas=new Counter();
        List<Thread> ts=new ArrayList<>(6000);
        long start =System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            Thread t=new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    cas.count();
                    cas.safeCount();
                }
            });
            ts.add(t);
        }
        for (Thread t : ts) {
            t.start();
        }
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(cas.i);
        System.out.println(cas.atomicInteger);
        System.out.println(System.currentTimeMillis()-start);
    }
    private void safeCount(){
        for (;;){
            int i=atomicInteger.get();
            boolean suc=atomicInteger.compareAndSet(i,++i);
            if (suc){
                break;
            }
        }
    }
    private void count(){
        i++;
    }

}
