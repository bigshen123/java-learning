package com.bigshen.learningDemo.JUC.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:A、B不会买到相同的票
 * @Author: BIGSHEN
 * @Date: 2019/12/1 16:59
 */
public class SellTickets {
    AtomicInteger tickes=new AtomicInteger(100);
    class Seller implements Runnable {

        @Override
        public void run() {
            while (tickes.get()>0){
                int tmp = tickes.get();
                if(tickes.compareAndSet(tmp,tmp-1)){
                    System.out.println(Thread.currentThread().getName()+"-->"+tmp);
                }
            }
        }
    }

    public static void main(String[] args) {
        SellTickets sellTickets=new SellTickets();
        new Thread(sellTickets.new Seller(),"SellerA").start();
        new Thread(sellTickets.new Seller(),"SellerB").start();

    }
}
