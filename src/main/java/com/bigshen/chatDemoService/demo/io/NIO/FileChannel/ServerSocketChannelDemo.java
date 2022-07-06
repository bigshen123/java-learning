package com.bigshen.chatDemoService.demo.io.NIO.FileChannel;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author BYJ
 * @Date 2022/5/17 10:46
 * @Describe
 */
public class ServerSocketChannelDemo {


    /** 服务端
     * 1.通过ServerSocketChannel 绑定ip地址和端口号
     * 2.通过ServerSocketChannelImpl的accept()方法创建一个SocketChannel对象用户从客户端读/写数据
     * 3.创建读数据/写数据缓冲区对象来读取客户端数据或向客户端发送数据
     * 4. 关闭SocketChannel和ServerSocketChannel
     */
    @Test
    public void ServerSocketChannelTest() {
        try {
            Selector selector = Selector.open();

            ServerSocketChannel serverSocketChannelOne = ServerSocketChannel.open();
            serverSocketChannelOne.socket().bind(new InetSocketAddress("127.0.0.1", 8080));
            serverSocketChannelOne.configureBlocking(false);
            //注册channel并指定监听的事件
            serverSocketChannelOne.register(selector, SelectionKey.OP_ACCEPT);

            ServerSocketChannel serverSocketChannelTwo = ServerSocketChannel.open();
            serverSocketChannelTwo.socket().bind(new InetSocketAddress("127.0.0.1", 8090));
            serverSocketChannelTwo.configureBlocking(false);
            //注册channel并指定监听的事件
            serverSocketChannelTwo.register(selector, SelectionKey.OP_ACCEPT);

            ByteBuffer readBuff = ByteBuffer.allocate(1024);
            ByteBuffer writeBuff = ByteBuffer.allocate(1024);
            writeBuff.put("received".getBytes());
            writeBuff.flip();

            while (true) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    if (selectionKey.isConnectable()) {
                        System.out.println(Thread.currentThread().getId() + "start Connectable....");
                    } else if (selectionKey.isAcceptable()) {
                        System.out.println(Thread.currentThread().getId() + "start Acceptable....");
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        readBuff.clear();
                        socketChannel.read(readBuff);

                        readBuff.flip();
                        System.out.println(Thread.currentThread().getId() + "-received:" + new String(readBuff.array()));
                        selectionKey.interestOps(SelectionKey.OP_WRITE);
                    } else if (selectionKey.isWritable()) {
                        writeBuff.rewind();
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        socketChannel.write(writeBuff);
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Scattering 将数据写入到 buffer时,可以采用buffer数组, 依次写入
     * Gathering: 从buffer读取数据时,也可采用buffer数组,依次读
     */
    @Test
    public void ScatteringAndGatheringTest() throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
        // 绑定端口到socket并启动
        System.out.println("启动了 =启动了");
        serverSocketChannel.socket().bind(inetSocketAddress);
        // 创建buffer数组
        System.out.println("执行了");
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);
        // 等客户端连接(telnet)
        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;
        // 循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long read = socketChannel.read(byteBuffers);
                // 累计读取的字节数
                byteRead += read;
                System.out.println("byteRead = " + byteRead);
                // 使用流打印, 看看当前buffer的 position 和limit
                Arrays.stream(byteBuffers).map(buffer -> "position" + buffer.position() + "    limit=" + buffer.limit()).forEach(System.out::println);

            }
            // 将所有的buffer进行反转
            Arrays.stream(byteBuffers).forEach(Buffer::flip);
            // 将数据读出显示回客户端
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
            }
            // 将所有的buffer进行复位
            Arrays.stream(byteBuffers).forEach(Buffer::clear);
            System.out.println("byteRead:=" + byteRead + "byteWrite=" + byteWrite + "messageLength=" + messageLength);
        }
    }
}
