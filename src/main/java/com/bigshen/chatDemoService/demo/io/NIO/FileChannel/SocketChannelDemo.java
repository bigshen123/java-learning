package com.bigshen.chatDemoService.demo.io.NIO.FileChannel;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Author BYJ
 * @Date 2022/5/17 10:46
 * @Describe
 */
public class SocketChannelDemo {

    /** 客户端
     * 1.通过SocketChannel连接到远程服务器
     * 2.创建读数据/写数据缓冲区对象来读取服务端数据或向服务端发送数据
     * 3.关闭SocketChannel
     * @throws IOException
     */
    @Test
    public void SocketChannelTest() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));

        ByteBuffer writeBuffer = ByteBuffer.allocate(32);
        ByteBuffer readBuffer = ByteBuffer.allocate(32);

        writeBuffer.put("hello".getBytes());
        writeBuffer.flip();
        while (true) {
            writeBuffer.rewind();
            socketChannel.write(writeBuffer);
            readBuffer.clear();
            socketChannel.read(readBuffer);
        }
    }
}
