package com.bigshen.learningDemo.netty.NIO;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * @author byj
 * @date 2025/3/20
 * @Description
 */
public class NIOServer {
    private static Selector selector;
    public static void main(String[] args) {
        init();
        listen();
    }
    private static void init() {
        //serverSocketChannel其实就是ServerSocket，负责跟各个客户端连接连接请求
        //SelectionKey.OP_ACCEPT的意思是仅仅关注ServerSocketChannel接收到的TCP连接请求，也就是监听ACCEPT事件
        //ServerSocketChannel是注册在Selector上面的
        ServerSocketChannel serverSocketChannel;
        try {
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();//步骤一：打开ServerSocketChannel
            serverSocketChannel.configureBlocking(false);//步骤二：NIO就是支持非阻塞的
            serverSocketChannel.socket().bind(new InetSocketAddress(9000), 100);//步骤二
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);//步骤三：注册到selector + 关注OP_ACCEPT事件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void listen() {
        while(true) {
            try {
                //select()方法是阻塞的，看注册到Selector上的ServerSocketChannel是否接收到了请求
                selector.select();//步骤四
                Iterator<SelectionKey> keysIterator = selector.selectedKeys().iterator();
                while(keysIterator.hasNext()) {
                    SelectionKey key = (SelectionKey) keysIterator.next();
                    //可以认为一个SelectionKey代表了一个请求
                    keysIterator.remove();
                    handleRequest(key);
                }
            } catch(Throwable t){
                t.printStackTrace();
            }
        }
    }

    private static void handleRequest(SelectionKey key) throws IOException, ClosedChannelException {
        //会有个线程池获取到了请求，下面的代码都应该在线程池的工作线程里处理
        SocketChannel channel = null;
        try {
            //如果这个SelectionKey是acceptable，也就是连接请求
            //那么注册对应的SocketChannel到selector上，并关注OP_READ事件
            if (key.isAcceptable()) {//步骤五
                System.out.println("[" + Thread.currentThread().getName() + "]接收到连接请求");
                //从SelectionKey中拿出ServerSocketChannel
                ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();

                //调用ServerSocketChannel的accept方法，就可以跟客户端进行TCP三次握手
                channel = serverSocketChannel.accept();
                System.out.println("[" + Thread.currentThread().getName() + "]建立连接时获取到的channel=" + channel);

                //如果三次握手成功了之后，就可以获取到一个建立好TCP连接的SocketChannel
                //这个SocketChannel大概可以理解为，底层有一个Socket是跟客户端进行连接的
                //这个SocketChannel就是联通到那个Socket上去，负责进行网络数据的读写的

                //下面配置成非阻塞的
                channel.configureBlocking(false);

                //将该SocketChannel注册到selector上，且仅仅关注READ请求，也就是关注发送数据过来的请求
                channel.register(selector, SelectionKey.OP_READ);//步骤六：注册到selector + 关注OP_READ事件
            } else if (key.isReadable()) {//步骤七
                //如果这个SelectionKey是readable，也就是发送了数据过来，此时需要读取客户端发送过来的数据
                channel = (SocketChannel) key.channel();
                //读取请求数据的buffer缓冲
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                //通过底层的socket读取数据，写入buffer中，position可能就会变成比如21之类的
                //socket读取到了多少个字节，此时buffer的position就会变成多少
                int count = channel.read(buffer);//步骤七
                System.out.println("[" + Thread.currentThread().getName() + "]接收到请求");

                if (count > 0) {
                    //设置position = 0，limit = 21，仅仅读取buffer中，0~21这段刚刚写入进去的数据
                    buffer.flip();
                    System.out.println("服务端接收请求：" + new String(buffer.array(), 0, count));
                    channel.register(selector, SelectionKey.OP_WRITE);//步骤七：注册到selector + 关注OP_WRITE事件
                }
            } else if (key.isWritable()) {//步骤八
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                buffer.put("Server收到了".getBytes());
                buffer.flip();

                channel = (SocketChannel) key.channel();
                channel.write(buffer);//步骤八
                channel.register(selector, SelectionKey.OP_READ);//步骤八：注册到selector + 关注OP_READ事件
            }
        } catch(Throwable t) {
            t.printStackTrace();
            if (channel != null) {
                channel.close();
            }
        }
    }
}
