package com.bigshen.learningDemo.JUC.lock;

public class DeadLock extends Thread {

  protected Object myDirect;
  static Object south = new Object();
  static Object north = new Object();

  public DeadLock(Object obj) {
    this.myDirect = obj;
    if (myDirect == south) {
      this.setName("south");
    }
    if (myDirect == north) {
      this.setName("north");
    }
  }

  public void run() {
    if (myDirect == south) {
      synchronized (north) {
        try {
          Thread.sleep(500);
        } catch (Exception e) {
          e.printStackTrace();
        }
        synchronized (south) {
          System.out.println("car to south has passed");
        }
      }
    }
    if (myDirect == north) {
      synchronized (south) {
        try {
          Thread.sleep(500);
        } catch (Exception e) {
          e.printStackTrace();
        }
        synchronized (north) {
          System.out.println("car to north has passed");
        }
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    DeadLock car2South = new DeadLock(south);
    DeadLock car2North = new DeadLock(north);
    car2South.start();
    car2North.start();
    Thread.sleep(1000);
  }
}
