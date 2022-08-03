package com.bigshen.learningDemo.concurrent.thread.bussiness;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/22 10:09
 */
class Bussiness {
    //子线程在使用的标记
    private boolean subFlag = true;

    synchronized void mainMethod() {
        while (subFlag) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 5; i++) {
            System.out.println(Thread.currentThread().getName()
                    + " : main thread running loop count -- " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        subFlag = true;
        notify();
    }

    synchronized void subMethod(){
        while (!subFlag){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+
                    " : sub thread running loop count -- " + i);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        subFlag=false;
        notify();
    }
}
