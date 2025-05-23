package com.bigshen.springbootDemo.mq.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ShutdownableThread extends Thread{
    private static final Logger log = LoggerFactory.getLogger(ShutdownableThread.class);
    private AtomicBoolean isRunning = new AtomicBoolean(true);
    private CountDownLatch shutdownLatch = new CountDownLatch(1);

    volatile public static UncaughtExceptionHandler funcaughtExceptionHandler = null;

    public ShutdownableThread(String name){
        this(name,true);
    }

    public ShutdownableThread(String name, boolean daemon) {
        super(name);
        this.setDaemon(daemon);
        if (funcaughtExceptionHandler != null)
            this.setUncaughtExceptionHandler(funcaughtExceptionHandler);
    }

    public abstract void execute();

    public boolean getRunning(){
        return isRunning.get();
    }

    @Override
    public void run() {
        try {
            execute();
        }catch (Error | RuntimeException e){
            log.error("Thread{} exiting with uncaught exception: ",getName(),e);
            throw e;
        }finally {
            shutdownLatch.countDown();
        }
    }

    public void shutdown(long gracefulTimeout,TimeUnit unit) throws InterruptedException{
        boolean success = gracefulShutdown(gracefulTimeout,unit);
        if(!success){
            forceShutdown();
        }
    }

    public boolean gracefulShutdown(long timeout,TimeUnit unit) throws InterruptedException {
        startGracefulShutdown();
        return awaitShutdown(timeout,unit);
    }


    public void startGracefulShutdown(){
        log.info("Starting graceful shutdown of thread{}",getName());
        isRunning.set(false);
    }

    public boolean awaitShutdown(long timeout, TimeUnit unit) throws InterruptedException {
        return shutdownLatch.await(timeout,unit);
    }

    public void forceShutdown() throws InterruptedException{
        log.info("Focing shutdown of thread{}",getName());
        isRunning.set(false);
        interrupt();
    }
}
