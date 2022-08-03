package com.bigshen.learningDemo.demo.io.NIO.FileChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.util.Iterator;

/**
 * @Author BYJ
 * @Date 2022/5/17 10:46
 * @Describe
 */
public class DatagramChannelDemo {
    public static class Server {
        public static void main(String[] args) throws IOException {
            Selector selector = Selector.open();
            DatagramChannel channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new InetSocketAddress(8080));
            channel.register(selector, SelectionKey.OP_READ);

            ByteBuffer byteBuffer = ByteBuffer.allocate(65535);
            while (true) {
                int n = selector.select();
                if (n > 0) {
                    Iterator iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = (SelectionKey) iterator.next();
                        //必须手动删除
                        iterator.remove();
                        if (selectionKey.isReadable()) {
                            DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
                            byteBuffer.clear();
                            //读取数据
                            InetSocketAddress inetSocketAddress = (InetSocketAddress) datagramChannel
                                    .receive(byteBuffer);
                            System.out.println(new java.lang.String(byteBuffer.array()));

                            //删除缓冲区的数据
                            byteBuffer.clear();
                            java.lang.String message = "data come from server";
                            byteBuffer.put(message.getBytes());
                            byteBuffer.flip();

                            //发送数据
                            datagramChannel.send(byteBuffer, inetSocketAddress);
                        }
                    }

                }
            }
        }
    }

    public static class Client {
        public static void main(String[] args) throws IOException {
            Selector selector = Selector.open();
            DatagramChannel datagramChannel = DatagramChannel.open();
            datagramChannel.configureBlocking(false);
            SocketAddress socketAddress = new InetSocketAddress("localhost", 8080);
            datagramChannel.connect(socketAddress);

            datagramChannel.register(selector, SelectionKey.OP_READ);
            datagramChannel.write(Charset.defaultCharset().encode("data come from client123!"));
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);

            while (true) {
                int n = selector.select();
                if (n > 0) {
                    Iterator iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = (SelectionKey) iterator.next();
                        iterator.remove();
                        if (selectionKey.isReadable()) {
                            datagramChannel = (DatagramChannel) selectionKey.channel();
                            datagramChannel.read(byteBuffer);
                            System.out.println(new String(byteBuffer.array()));

                            byteBuffer.clear();
                            datagramChannel.write(Charset.defaultCharset()
                                    .encode("data come from client456!"));
                        }
                    }
                }
            }
        }
    }
}
