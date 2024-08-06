package com.bigshen.learningDemo.JUC.join;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author BYJ
 * @Date 2020/12/17 21:54
 * @Describe
 */
@Slf4j
public class JoinTest {
    private static void join() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.info("running");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }) ;

        Thread t2=new Thread(()->{
            log.info("running2");
            try{
                Thread.sleep(4000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        //等待线程1终止
        t1.join();

        //等待线程2终止
        t2.join();
        log.info("main over");
    }

    public static void main(String[] args) throws InterruptedException {
        join();
    }
}
