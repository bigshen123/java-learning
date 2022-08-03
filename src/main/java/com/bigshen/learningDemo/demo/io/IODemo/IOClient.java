package com.bigshen.learningDemo.demo.io.IODemo;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/26 20:37
 */
public class IOClient {
    public static void main(String[] args) {
        // TODO 创建多个线程，模拟多个客户端连接服务端
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 3333);
                while (true) {
                    try {
                        socket.getOutputStream().write((new Date() + ": hello world").getBytes());
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
            } catch (IOException e) {
            }
        }).start();
    }
}
