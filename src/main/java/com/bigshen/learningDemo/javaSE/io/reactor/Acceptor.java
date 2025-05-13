package com.bigshen.learningDemo.javaSE.io.reactor;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author BYJ
 * @Date 2024/4/28 20:52
 * @Describe 在Acceptor获取到客户端连接之后，其就将其交由线程池进行网络读写了，而这里的主线程只是不断监听客户端连接事件。
 *
 */
public class Acceptor implements Runnable {
    private final ExecutorService executor = Executors.newFixedThreadPool(20);

    private final ServerSocketChannel serverSocket;

    public Acceptor(ServerSocketChannel serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        try {
            // 获取客户端连接
            SocketChannel channel = serverSocket.accept();
            if (null != channel) {
                // 将客户端连接交由线程池处理
                executor.execute(new Handler(channel));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}