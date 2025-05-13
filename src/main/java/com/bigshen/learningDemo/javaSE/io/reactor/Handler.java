package com.bigshen.learningDemo.javaSE.io.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author BYJ
 * @Date 2024/4/28 20:53
 * @Describe 在Handler中，主要进行的就是为每一个客户端 Channel创建一个Selector，并且监听该Channel的网络读写事件。
 * 当有事件到达时，进行数据的读写，而业务操作交由具体的业务线程池处理。
 */
public class Handler implements Runnable {
    private final SocketChannel channel;
    private final Selector selector;
    private final ByteBuffer input = ByteBuffer.allocate(1024);
    private final ByteBuffer output = ByteBuffer.allocate(1024);

    public Handler(SocketChannel channel) throws IOException {
        this.channel = channel;
        this.channel.configureBlocking(false); // 设置非阻塞
        this.selector = Selector.open();
        this.channel.register(selector, SelectionKey.OP_READ);
    }

    @Override
    public void run() {
        try {
            while (selector.isOpen() && channel.isOpen()) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isReadable()) {
                        read(key);
                    } else if (key.isWritable()) {
                        write(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) throws IOException {
        input.clear();
        int bytesRead = channel.read(input);
        if (bytesRead <= 0) {
            return;
        }
        input.flip();

        byte[] bytes = new byte[input.remaining()];
        input.get(bytes);
        String message = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("收到客户端消息: " + message);

        // 处理业务逻辑
        output.clear();
        output.put(("服务器收到: " + message).getBytes(StandardCharsets.UTF_8));
        output.flip();

        key.interestOps(SelectionKey.OP_WRITE); // 改为监听写事件
    }

    private void write(SelectionKey key) throws IOException {
        channel.write(output);
        if (!output.hasRemaining()) {
            output.clear();
            key.interestOps(SelectionKey.OP_READ); // 写完后重新监听读事件
        }
    }
}