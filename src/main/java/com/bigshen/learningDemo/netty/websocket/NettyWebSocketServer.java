package com.bigshen.learningDemo.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author byj
 * @date 2025/3/21
 * @Description 基于WebSocket协议开发NettyServer
 * WebSocket和Netty Server配合起来开发说明：
 *
 * 如果Server端要主动推送一些通知(push)给网页端正在浏览网页的用户，如推送用户可能感兴趣的商品、关注的新闻。那么在用户进入网页后可以询问用户，
 * 是否愿意收到服务端发送的xx提示和通知。如果用户愿意，那么网页里的WebSocket完全可以跟NettyServer构建一个长连接。
 * 这样NettyServer在必要时，就可以反向推送通知(push)给用户，浏览器里的网页可能会弹出一个push通知用户xx讯息。
 *
 * <p>
 * 可以基于TCP协议用Netty来开发客户端和服务端进行相互通信，但粘包半包问题需要手动进行处理。
 * <p>
 * 可以基于HTTP协议用Netty来开发一个HTTP服务器，服务器接收浏览器发送过来的HTTP请求后，返回HTTP响应回浏览器。
 * <p>
 * 也可以基于WebSocket协议来开发一个Netty服务器，此时前端HTML代码会基于socket协议和Netty服务器建立长连接。
 * 这样Netty服务器就可以和浏览器里运行的HTML通过WebSocket协议建立长连接，从而使得Netty服务器可以主动推送数据给浏览器里的HTML页面。
 * <p>
 * WebSocket协议底层也是基于TCP协议来实现的，只不过是在TCP协议的基础上封装了一层更高层次的WebSocket协议。
 */
public class NettyWebSocketServer {
    private static final Logger logger = LogManager.getLogger(NettyWebSocketServer.class);
    private static final int DEFAULT_PORT = 8998;
    private final int port;

    public NettyWebSocketServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        logger.info("Netty WebSocket Server is starting.");
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline()
                                    //浏览器的字节数组数据进来以后，字节数组数据先用http协议来处理，把字节数组转换为一个HttpRequest请求对象
                                    //最后数据返回给浏览器前，又会对HttpResponse对象进行编码成字节数组
                                    .addLast(new HttpServerCodec())
                                    //chunked write用于大量数据流时的分chunk块，也就是数据实在是太大了就必须得分chunk
                                    //大量数据流进来时，可以分chunk块来读；大量数据流出去时，可以分chunk块来写；
                                    .addLast(new ChunkedWriteHandler())
                                    //如果想要让很多http不要拆分成很多段过来，可以把完整的请求数据聚合到一起再给过来
                                    .addLast(new HttpObjectAggregator(1024 * 32))
                                    //基于前面已经转换好的请求数据对象，会在这里基于WebSocket协议再次做一个处理
                                    //由于传输时是基于http协议传输过来的，而封装的内容是按webSocket协议来封装的http请求数据
                                    //所以必须在这里提取http请求里面的数据，然后按照WebSocket协议来进行解析处理，把数据提取出来作为WebSocket的数据片段
                                    .addLast(new WebSocketServerProtocolHandler("/websocket"))
                                    //响应数据输出时，顺序是反的，第一步原始数据必须先经过WebSocket协议转换
                                    //WebSocket协议数据，必须经过HTTP协议处理，但最终会encode编码成一个HTTP协议的响应数据
                                    //然后服务端将HTTP响应数据序列化的字节数组，传输给浏览器
                                    //浏览器拿到字节数组后进行反序列化，拿到一个HTTP协议响应数据，提取出内容再按照WebSocket协议来处理
                                    //最终把普通的数据给WebSocket代码
                                    .addLast("netty-web-socket-server-handler", new NettyWebSocketServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("Netty WebSocket Server server is started, listened[" + port + "].");
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        NettyWebSocketServer nettyHttpServer = new NettyWebSocketServer(DEFAULT_PORT);
        nettyHttpServer.start();
    }

}
