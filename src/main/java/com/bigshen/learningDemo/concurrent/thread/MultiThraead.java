package com.bigshen.learningDemo.concurrent.thread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

/**
 * @ClassName MultiThraead
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/5/24
 * @Version V1.0
 **/
public class MultiThraead {
    public static void main(String[] args) {
        ThreadMXBean threadMXBean= ManagementFactory.getThreadMXBean();
        ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads(false, false);
        for (ThreadInfo threadInfo : threadInfos) {
            /**
             * [6]Monitor Ctrl-Break 监听中断的线程
             * [5]Attach Listener  附加监听的线程
             * [4]Signal Dispatcher 分发处理给JVM信号的线程
             * [3]Finalizer 调用对象finalizer的线程
             * [2]Reference Handler 清除的线程
             * [1]main  main线程，用户程序入口
             */
            System.out.println("["+threadInfo.getThreadId()+"]"+threadInfo.getThreadName());
        }
    }
}
