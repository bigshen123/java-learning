package com.bigshen.learningDemo.concurrent.semaphore;

/**
 * @Description:
 * @Author: byj
 * @Date: 2019/12/4 14:08
 */
public class MyThread extends Thread {
    //线程的名称
    private String threadname;
    //自定义池
    private MyPool pool;
    //申请信号量的大小
    private int x;

    MyThread(String threadname, MyPool pool, int x) {
        this.threadname = threadname;
        this.pool = pool;
        this.x = x;
    }

    @Override
    public void run() {
        try {
            //从此信号量获取给定数目的许可
            pool.getSp().acquire(x);
            //todo：也许这里可以做更复杂的业务
            System.out.println(threadname + "成功获取了" + x + "个许可！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //释放给定数目的许可，将其返回到信号量。
            pool.getSp().release(x);
            System.out.println(threadname + "释放了" + x + "个许可！");
        }
    }
}
