package com.bigshen.learningDemo.netty.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author byj
 * @date 2025/3/20
 * @Description
 */
public class NIOClient {
    public static void main(String[] args) {
        //启动10个线程去和服务端建立通信
        for (int i = 0; i < 10; i++) {
            new Worker().start();
        }
    }

    static class Worker extends Thread {
        @Override
        public void run() {
            SocketChannel channel = null;
            Selector selector = null;
            try {
                //SocketChannel底层就是封装了一个Socket
                //SocketChannel是通过底层的Socket网络连接上服务端的数据通道，负责基于网络读写数据
                channel = SocketChannel.open();//步骤一
                //配置成非阻塞的
                channel.configureBlocking(false);//步骤二
                //底层会发起了一个TCP三次握手尝试建立连接
                channel.connect(new InetSocketAddress("localhost", 9000));//步骤三

                selector = Selector.open();
                //监听connect行为
                channel.register(selector, SelectionKey.OP_CONNECT);//步骤四：注册到selector + 关注OP_CONNECT事件

                while(true) {
                    //三次握手成功后，服务端会给客户端返回一个响应请求，通过如下代码把响应请求拿到
                    selector.select();//步骤五
                    Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();

                    while(keysIterator.hasNext()) {
                        SelectionKey key = (SelectionKey) keysIterator.next();
                        keysIterator.remove();

                        //如果server返回的是一个connectable的消息
                        if (key.isConnectable()) {//步骤六
                            channel = (SocketChannel) key.channel();
                            if (channel.isConnectionPending()) {
                                //一旦建立连接成功了以后，此时就可以给server发送一个数据了
                                channel.finishConnect();
                                ByteBuffer buffer = ByteBuffer.allocate(1024);
                                buffer.put("你好".getBytes());
                                buffer.flip();
                                channel.write(buffer);
                            }
                            //接下来监听READ事件就是准备读服务端返回的数据
                            channel.register(selector, SelectionKey.OP_READ);//步骤六：注册到selector + 关注OP_READ事件
                        } else if (key.isReadable()) {//步骤七：说明服务器端返回了一条数据可以读了
                            channel = (SocketChannel) key.channel();
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            int len = channel.read(buffer);//把数据写入buffer，position推进到读取的字节数数字
                            if (len > 0) {
                                System.out.println("[" + Thread.currentThread().getName() + "]收到响应：" + new String(buffer.array(), 0, len));
                                Thread.sleep(5000);
                                //睡眠5秒后，接下来继续监听WRITE事件，并准备写数据给服务端
                                channel.register(selector, SelectionKey.OP_WRITE);//步骤七：注册到selector + 关注OP_WRITE事件
                            }
                        } else if (key.isWritable()) {//步骤八
                            ByteBuffer buffer = ByteBuffer.allocate(1024);
                            buffer.put("重复你好了".getBytes());
                            buffer.flip();

                            channel = (SocketChannel) key.channel();
                            channel.write(buffer);
                            //再次重复发数据给服务端后，接下来要监听READ事件就是准备读服务端返回的数据
                            channel.register(selector, SelectionKey.OP_READ);//步骤八：注册到selector + 关注OP_READ事件
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (channel != null){
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (selector != null) {
                    try {
                        selector.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
