package com.bigshen.learningDemo.concurrent.stop;

public class StopTread {

  public static void main(String[] args) throws InterruptedException {
    ChangeObjectThread t = new ChangeObjectThread();
    t.start();
    Thread.sleep(150);
    t.stopMe();
  }

  public static class ChangeObjectThread extends Thread {

    volatile boolean stopme = false;
    //boolean stopme = false;

    public void stopMe() {
      stopme = true;
    }

    @Override
    public void run() {
      while (true) {
        if (stopme) {
          System.out.println("exit by stop me");
          break;
        }
        System.out.println("ChangeObjectThread Running");
        //TODO 做一些业务操作，在此省略
        Thread.yield();
      }
    }
  }

}
