package com.bigshen.learningDemo.concurrent.join;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * @Author BYJ
 * @Date 2020/12/17 21:54
 * @Describe
 */
public class JoinTest {
    private static void join() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            LOGGER.info("running");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }) ;

        Thread t2=new Thread(()->{
            LOGGER.info("running2");
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
        LOGGER.info("main over");
    }

    public static void main(String[] args) throws InterruptedException {
        join();
    }
}
