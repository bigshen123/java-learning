package com.bigshen.learningDemo.netty.websocket.push;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author byj
 * @date 2025/5/14
 * @Description Netty实现的消息推送系统
 * 首先需要一个运营系统能够基于NettyClient和PushServer建立WebSocket长连接，然后浏览器客户端也要和PushServer建立好WebSocket长连接，
 * 接着运营系统会让NettyClient发送Push推送消息给PushServer，最后PushServer再把推送消息发送给浏览器客户端。
 * 首先启动PushServer，然后打开多个网页客户端查看console，接着启动运营客系统在控制台输入消息，这样就可以完成一个完整的消息推送的交互了。
 * <p>
 * 消息推送系统的PushServer
 */
public class NettyPushServer {
    private static final Logger logger = LogManager.getLogger(NettyPushServer.class);
    private static final int DEFAULT_PORT = 8998;
    private final int port;

    public NettyPushServer(int port) {
        this.port = port;
    }

    public void start() throws Exception {
        logger.info("Netty Push Server is starting.");
        EventLoopGroup bossEventLoopGroup = new NioEventLoopGroup();
        EventLoopGroup workerEventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast("logging", new LoggingHandler("DEBUG"))
                                    .addLast("http-codec", new HttpServerCodec())
                                    .addLast("aggregator", new HttpObjectAggregator(65536))
                                    .addLast("http-chunked", new ChunkedWriteHandler())
                                    .addLast("netty-push-server-handler", new NettyPushServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            logger.info("Netty Push Server is started, listened[" + port + "].");
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossEventLoopGroup.shutdownGracefully();
            workerEventLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        NettyPushServer nettyHttpServer = new NettyPushServer(DEFAULT_PORT);
        nettyHttpServer.start();
    }
}
