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
 * @date 2025/3/26
 * @Description  select 和 poll 通过遍历所有的 socket，检查是否有数据可读或可写。
 */
public class NIOSelectorServer {
    public static void main(String[] args) throws IOException {
        // 1. 创建 Selector
        Selector selector = Selector.open();

        // 2. 创建 ServerSocketChannel，并绑定端口
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(8080));
        serverChannel.configureBlocking(false); // 设置非阻塞模式

        // 3. 注册到 Selector，监听“连接事件”
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server started on port 8080...");

        while (true) {
            // 4. 监听事件，阻塞等待（超时1秒）
            if (selector.select(1000) == 0) {
                continue; // 无事件发生，继续轮询
            }

            // 5. 遍历已就绪的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove(); // 避免重复处理

                // 6. 处理“连接就绪”事件
                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel clientChannel = server.accept(); // 接收连接
                    clientChannel.configureBlocking(false);
                    clientChannel.register(selector, SelectionKey.OP_READ); // 监听“可读事件”
                    System.out.println("New client connected: " + clientChannel.getRemoteAddress());
                }

                // 7. 处理“可读”事件
                if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int bytesRead = client.read(buffer);
                    if (bytesRead > 0) {
                        buffer.flip();
                        String message = new String(buffer.array(), 0, bytesRead);
                        System.out.println("Received: " + message);
                        buffer.clear();
                    } else if (bytesRead == -1) {
                        client.close(); // 关闭连接
                    }
                }
            }
        }
    }
}
