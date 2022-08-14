package com.bigshen.learningDemo.concurrent.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPoolTest {

  public static class MyTask implements Runnable {

    @Override
    public void run() {
      System.out.println("Thread ID:" + Thread.currentThread().getId());
      try {
        Thread.sleep(5000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    MyTask task = new MyTask();
    ExecutorService es = Executors.newFixedThreadPool(5);
    for (int i = 0; i < 10; i++) {
      Future<?> submit = es.submit(task);
      System.out.println(submit);
    }
  }


}
