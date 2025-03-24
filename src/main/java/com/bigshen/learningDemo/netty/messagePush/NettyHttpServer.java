package com.bigshen.learningDemo.netty.messagePush;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author byj
 * @date 2025/3/21
 * @Description Netty实现的HTTP服务器
 */
public class NettyHttpServer {
    private static final Logger logger = LogManager.getLogger(NettyHttpServer.class);
    private static final int DEFAULT_PORT = 8998;
    private int port;

    public NettyHttpServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        logger.info("Netty Http Server is starting.");
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer() {
                        @Override
                        protected void initChannel(Channel ch) {
                            ch.pipeline()
                                    //数据进来是自上而下，数据回去时自下而上

                                    // 首先添加HTTP请求消息解码器 HttpRequestDecoder，因为浏览器会把按照HTTP协议组织起来的请求数据序列化成
                                    // 字节数组发送给服务器，而HttpRequestDecoder可以按照HTTP协议从接收到的字节数组中读取出一个完整的请求数据。
                                    .addLast("http-decoder", new HttpRequestDecoder())
                                    // 然后添加 HttpObjectAggregator解码器，它的作用是将多个消息转换为单一的FullHttpRequest或者FullHttpResponse
                                    .addLast("http-aggregator", new HttpObjectAggregator(65536))
                                    // 接着添加HTTP响应消息编码器 HttpResponseEncoder，它的作用是对HTTP响应消息进行编码。
                                    .addLast("http-encoder", new HttpResponseEncoder())
                                    // 以及添加 ChunkedWriteHandler处理器，用来支持异步发送大的码流时也不会占用过多的内存，从而防止内存溢出。
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    // 最后添加 NettyHttpServerHandler处理器，用于处理HTTP服务器的响应输出。
                                    .addLast("netty-http-server-handler", new NettyHttpServerHandler());
                        }
                    });

            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("Netty Http Server is started, listened[" + port + "].");
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        NettyHttpServer nettyHttpServer = new NettyHttpServer(DEFAULT_PORT);
        nettyHttpServer.start();
    }


}
