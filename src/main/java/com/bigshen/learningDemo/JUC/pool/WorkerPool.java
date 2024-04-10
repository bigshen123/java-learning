package com.bigshen.learningDemo.JUC.pool;

/**
 * @Author BYJ
 * @Date 2022/6/2 15:07
 * @Describe
 */

import java.util.concurrent.*;


public class WorkerPool {

    public static void main(String[] args) throws InterruptedException {

        //RejectedExecutionHandler implementation
        RejectedExecutionHandlerImpl rejectionHandler = new RejectedExecutionHandlerImpl();
        //Get the ThreadFactory implementation to use
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        //creating the ThreadPoolExecutor
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(2, 4, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), threadFactory, rejectionHandler);
        //start the monitoring thread
        MyMonitorThread monitor = new MyMonitorThread(executorPool, 3);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();
        //submit work to the thread pool
        for (int i = 0; i < 10; i++) {
            executorPool.execute(new WorkerThread("cmd" + i));
        }

        Thread.sleep(30000);
        //shut down the pool
        executorPool.shutdown();
        //shut down the monitor thread
        Thread.sleep(5000);
        monitor.shutdown();

    }

    public static class RejectedExecutionHandlerImpl implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(r.toString() + " is rejected");
        }

    }

    public static class WorkerThread implements Runnable {

        private final String command;

        public WorkerThread(String s) {
            this.command = s;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + " Start. Command = " + command);
            processCommand();
            System.out.println(Thread.currentThread().getName() + " End.");
        }

        private void processCommand() {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String toString() {
            return this.command;
        }
    }
}
