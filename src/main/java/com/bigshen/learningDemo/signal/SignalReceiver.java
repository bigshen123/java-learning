package com.bigshen.learningDemo.signal;

import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 * @Author BYJ
 * @Date 2023/11/17 21:13
 * @Describe 参考：https://blog.csdn.net/pleaseprintf/article/details/132549826
 * 通过使用Signal类，Java项目可以实现信号的连续接收，从而在接收到特定信号时执行相应的逻辑
 * 这对于处理异步事件或者优雅地关闭应用程序非常有用。
 */
public class SignalReceiver {
    public static void main(String[] args) {
        SignalHandler handler1 = signal -> {
            System.out.println("Received signal: " + signal.getName());
            // 在这里执行信号处理逻辑
        };
        // Ctrl+C信号
        Signal.handle(new Signal("INT"), handler1);
        // 终止信号
        Signal.handle(new Signal("TERM"), handler1);

        System.out.println("Waiting for signals. Press Ctrl+C to send SIGINT...");
        while (true) {
            // 持续运行，等待信号
        }
    }
}
