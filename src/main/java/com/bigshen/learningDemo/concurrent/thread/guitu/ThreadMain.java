package com.bigshen.learningDemo.concurrent.thread.guitu;

/**
 * @Description:使用多线程，模拟龟兔赛跑的场景。
 *      按照以往的故事经验，正确的故事逻辑顺序当然是因为兔子不把乌龟放在眼里，松懈了最终输了比赛。
 *      我的设定也是如此，假定乌龟每秒跑五米（是有点快但为了线程的运行速度来看还是写快点）兔子的速度为每秒十米。
 *      但是兔子在跑到一半休息了一点五秒。
 * @Author: BIGSHEN
 * @Date: 2019/12/15 18:47
 */
public class ThreadMain {
        public static void main(String[] args) {
            ThreadTest1 threadTest1 = new ThreadTest1();
            ThreadTest2 threadTest2 = new ThreadTest2();
            Thread thread1 = new Thread(threadTest1);
            Thread thread2 = new Thread(threadTest2);
            thread1.start();
            thread2.start();
    }
}
