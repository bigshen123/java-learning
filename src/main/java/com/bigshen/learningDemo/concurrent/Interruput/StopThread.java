package com.bigshen.learningDemo.concurrent.Interruput;

import java.io.IOException;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.sun.xml.internal.ws.spi.db.BindingContextFactory.LOGGER;

/**
 * @Author BYJ
 * @Date 2020/12/21 20:29
 * @Describe
 */
public class StopThread implements Runnable{

    @Override
    public void run() {
        while ( !Thread.currentThread().isInterrupted()) {
            // 线程执行具体逻辑
            System.out.println(Thread.currentThread().getName() + "运行中。。");
        }

        System.out.println(Thread.currentThread().getName() + "退出。。");
    }




    public static void main(String[] args) throws InterruptedException, IOException {
        /*Thread thread = new Thread(new StopThread(), "thread A");
        thread.start();

        System.out.println("main 线程正在运行") ;

        TimeUnit.MILLISECONDS.sleep(10) ;
        thread.interrupt();*/
        //executorService();
        piped();
    }


    public static void executorService() throws InterruptedException {
        BlockingQueue<Runnable> queue=new LinkedBlockingQueue<>(10);
        ThreadPoolExecutor poolExecutor=new ThreadPoolExecutor(5,5,1,TimeUnit.MICROSECONDS,queue);
        poolExecutor.execute(() -> {
            LOGGER.info("running");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        poolExecutor.execute(() -> {
            LOGGER.info("runnging2");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        poolExecutor.shutdown();
        while (!poolExecutor.awaitTermination(1,TimeUnit.MICROSECONDS)){
            LOGGER.info("线程还在执行");
        }
        LOGGER.info("main over!");
    }

    public static void piped() throws IOException {
        //面向于字符 PipedInputStream 面向于字节
        PipedWriter writer = new PipedWriter();
        PipedReader reader = new PipedReader();

        //输入输出流建立连接
        writer.connect(reader);


        Thread t1 = new Thread(() -> {
            System.out.println("running");
            try {
                for (int i = 0; i < 10; i++) {

                    writer.write(i+"");
                    Thread.sleep(10);
                }
            } catch (Exception e) {

            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
        Thread t2 = new Thread(() -> {
            System.out.println("running2");
            int msg = 0;
            try {
                while ((msg = reader.read()) != -1) {
                    System.out.println("msg="+(char) msg);
                }

            } catch (Exception e) {

            }
        });
        t1.start();
        t2.start();
    }

}
