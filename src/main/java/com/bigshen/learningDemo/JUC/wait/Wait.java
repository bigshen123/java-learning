package com.bigshen.learningDemo.JUC.wait;

public class Wait extends Thread{
  static Object object = new Object();

  public Wait(Object object) {
    this.object = object;
  }

  public void run()
  {
    //object.wait();
    synchronized (object) {
      System.out.println("Wait start!");
      try {
        object.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Wait end!");
    }
  }
}
