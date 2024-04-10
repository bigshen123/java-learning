package com.bigshen.learningDemo.JUC.thirdCode.thread.A1B2.method04;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @Author BYJ
 * @Date 2020/12/16 22:23
 * @Describe 阻塞队列实现
 */
public class ThreadDemo4 {
    private static final String[] charArr = new String[]{"A","B","C","D","E","F","G","H","I","J","K","L","M","N",
            "O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    private static final int[] numArr = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24, 25, 26};
    private static Thread thread1 = null;
    private static Thread thread2 = null;


    /**
     * 定义两个q，他们的特点就是手递手传递，队列里必须有内容，没有内容就会阻塞
     */
    static BlockingQueue<String> q1 = new ArrayBlockingQueue(1);
    static BlockingQueue<String> q2 = new ArrayBlockingQueue(1);

    public static void main(String[] args) throws Exception {
        thread1 = new Thread(()->{
            for (String c : charArr) {
                System.out.print(c);
                try {
                    // 给队列2放点东西，这样队列2就不阻塞了
                    q2.put("ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    // 去队列1取东西，没东西就在这阻塞
                    q1.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread2 = new Thread(()->{
            for (int n : numArr) {
                try {
                    // 上队列2里取东西，如果没有东西，就阻塞，不往下执行
                    q2.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.print(n);
                try {
                    // 往队列1里放东西，队列1就不阻塞了
                    q1.put("ok");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}

