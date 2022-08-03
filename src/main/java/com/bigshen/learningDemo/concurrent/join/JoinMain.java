package com.bigshen.learningDemo.concurrent.join;

public class JoinMain {

  public static int i = 0;

  public static class AddThread extends Thread {

    @Override
    public void run() {
      for (i = 0; i < 10000000; i++) {
        ;
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    AddThread at = new AddThread();
    at.setDaemon(true);
    at.start();
    at.join();
    System.out.println(i);
  }
}

