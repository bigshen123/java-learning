package com.bigshen.learningDemo.concurrent.Interruput;

public class InterruputSleepThread {

  static int i = 0;

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new Thread() {
      @Override
      public void run() {
        while (true) {
          if (Thread.currentThread().isInterrupted()) {
            System.out.println("Interruted!");
            break;
          }else{
            System.out.println("Running");
          }
          try {
            Thread.sleep(2000);
          } catch (InterruptedException e) {
            System.out.println("Interruted When Sleep,Interruted was cleared!");
          }
          Thread.yield();
        }
      }
    };
    t1.start();
    Thread.sleep(2000);
    t1.interrupt();
  }


}
