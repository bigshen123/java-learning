package com.bigshen.learningDemo.javaSE.io.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author BYJ
 * @Date 2024/4/28 20:52
 * @Describe Reactor模型，服务端主要有四个角色：Client、Reactor，Acceptor和Handler

 * 这里Reactor首先开启了一个 ServerSocketChannel，然后将其绑定到指定的端口，并且注册到了一个多路复用器上。
 * 接着在一个线程中，其会在多路复用器上等待客户端连接。当有客户端连接到达后，Reactor就会将其派发给一个Acceptor，由该Acceptor专门进行客户端连接的获取。
 */
public class Reactor implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException {
        serverSocket = ServerSocketChannel.open();  // 创建服务端的ServerSocketChannel
        serverSocket.configureBlocking(false);  // 设置为非阻塞模式
        // 创建一个Selector多路复用器
        selector = Selector.open();
        SelectionKey key = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        serverSocket.bind(new InetSocketAddress(port));  // 绑定服务端端口
        key.attach(new Acceptor(serverSocket));  // 为服务端Channel绑定一个Acceptor
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                // 服务端使用一个线程不断等待客户端的连接到达
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());  // 监听到客户端连接事件后将其分发给Acceptor
                    iterator.remove();
                }

                selector.selectNow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey key) throws IOException {
        // 这里的attachement也即前面为服务端Channel绑定的Acceptor，调用其run()方法进行
        // 客户端连接的获取，并且进行分发
        Runnable attachment = (Runnable) key.attachment();
        attachment.run();
    }

    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(8954);
        new Thread(reactor).start();
        System.out.println("Reactor server started on port 8954...");
    }
}
