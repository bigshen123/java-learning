package com.bigshen.learningDemo.JUC.thirdCode.thread.threadLocal;

/**
 * @Description:多线程中共享数据
 * @Author: BIGSHEN
 * @Date: 2019/12/22 11:01
 */
class ShareData {
    private int num=10;
    synchronized void inc(){
        num++;
        System.out.println(Thread.currentThread().getName()+":invoke inc method num="+num);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
class RunnableCusToInc implements Runnable{

    private ShareData shareData;
    private RunnableCusToInc(ShareData shareData){
        this.shareData=shareData;
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            shareData.inc();
        }
    }
    /**
     *测试方法
     **/
    public static void main(String[] args) {
        ShareData shareData = new ShareData();
        for (int i = 0; i < 4; i++) {
            new Thread(new RunnableCusToInc(shareData),"Thread "+ i).start();
        }
    }
}
