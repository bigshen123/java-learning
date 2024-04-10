package com.bigshen.learningDemo.JUC.thirdCode.thread.A1B2.method01;

/**
 * @Author BYJ
 * @Date 2020/12/16 22:03
 * @Describe
 */
public class Thread2 extends Thread {

    private final Object lock;

    public Thread2(Object lock){
        this.lock=lock;
    }

    @Override
    public void run(){
        String[] strings={"1","2","3","4"};
        for (String string : strings) {
            synchronized (lock) {
                lock.notify();
                try {
                    lock.wait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                System.out.print(string);
                lock.notify();
            }
        }
    }
}
