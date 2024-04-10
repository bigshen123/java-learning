package com.bigshen.learningDemo.JUC.wait;

public class WaitNotify {

  final static Object object = new Object();
  public static void main(String[] args) throws InterruptedException {
    Thread w = new Wait(object) ;
    Thread n = new Notify(object) ;
    w.start();
    n.start();
    w.join();
    n.join();
  }

  public static void main2(String[] args) throws InterruptedException {
    Thread w1 = new Wait(object) ;
    Thread w2 = new Wait(object) ;
    w2.start();
    w1.start();
    Thread.sleep(1000);
    Thread n2 = new Notify(object) ;
    n2.start();
    w1.join();
    w2.join();
    n2.join();
  }


}
