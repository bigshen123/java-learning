package com.bigshen.learningDemo.JUC.wait;

public class Notify extends Thread {

  static Object object = new Object();

  public Notify(Object object) {
    this.object = object;
  }

  public void run() {
    //object.notify();
    synchronized (object) {
      System.out.println("Notify start!");
      object.notify();
      //object.notifyAll();
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
      }
      System.out.println("Notify end!");
    }
  }
}
