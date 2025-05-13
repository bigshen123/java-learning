package com.bigshen.learningDemo.netty.NIO.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author byj
 * @date 2025/5/9
 * @Description 实现一个 Java NIO 的多路复用服务器端，
 * 它：使用 Selector 监听多个客户端连接；
 * 有客户端连进来就接受它；
 * 有数据就读取；
 * 不需要一个线程一个连接，一个线程搞定所有IO。
 */
public class NioEchoServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建 ServerSocketChannel 并设置为非阻塞
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(12345));
        serverChannel.configureBlocking(false);

        // 2. 创建Selector
        Selector selector = Selector.open();

        // 3. 将ServerChannel注册到Selector，监听“接受连接”事件
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started on port 12345...");

        // 4. 进入事件轮询（单线程处理所有连接）
        while (true) {
            selector.select(); // 阻塞直到有事件就绪
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                keyIterator.remove(); // 移除已处理的事件

                if (key.isAcceptable()) {
                    // 接收新连接
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    client.register(selector, SelectionKey.OP_READ);
                    System.out.println("Accepted new connection from " + client.getRemoteAddress());

                } else if (key.isReadable()) {
                    // 读取客户端数据
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = client.read(buffer);
                    if (read > 0) {
                        buffer.flip();
                        client.write(buffer); // 回显数据
                    } else if (read == -1) {
                        client.close();
                        System.out.println("Client closed connection");
                    }
                }
            }
        }
    }
}
