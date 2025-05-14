package com.bigshen.learningDemo.netty.websocket.push;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketClientCompressionHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * @author byj
 * @date 2025/5/14
 * @Description 消息推送系统的运营客户端
 * 运营客户端连接PushServer
 */
public class OperationNettyClient {
    private static final Logger logger = LogManager.getLogger(OperationNettyClient.class);
    private static final String WEB_SOCKET_SCHEME = "ws";
    private static final String WSS_SCHEME = "wss";
    private static final String LOCAL_HOST = "127.0.0.1";
    private static final String PUSH_SERVER_URI = System.getProperty("url", "ws://127.0.0.1:8998/push");

    private static URI uri;
    private static String host;
    private static int port;
    private static SslContext sslContext;
    private static final String INPUT_MESSAGE_QUIT = "quit";
    private static final String INPUT_MESSAGE_CLOSE = "close";
    private static final String INPUT_MESSAGE_PING = "ping";

    private EventLoopGroup eventLoopGroup;

    public Channel start() throws Exception {
        logger.info("Operation Netty Client is connecting.");
        eventLoopGroup = new NioEventLoopGroup();

        WebSocketClientHandshaker webSocketClientHandshaker = WebSocketClientHandshakerFactory.newHandshaker(uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders());
        final OperationNettyClientHandler operationNettyClientHandler = new OperationNettyClientHandler(webSocketClientHandshaker);

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline channelPipeline = ch.pipeline();
                        if (sslContext != null) {
                            channelPipeline.addLast(sslContext.newHandler(ch.alloc(), host, port));
                        }
                        channelPipeline.addLast(new HttpClientCodec())
                                .addLast(new HttpObjectAggregator(65536))
                                .addLast(WebSocketClientCompressionHandler.INSTANCE)
                                .addLast(operationNettyClientHandler);
                    }
                });
        Channel channel = bootstrap.connect(uri.getHost(), port).sync().channel();
        logger.info("Operation Netty Client connected to push server.");
        operationNettyClientHandler.channelFuture().sync();

        return channel;
    }

    private static String getScheme(URI pushServerUri) {
        return pushServerUri.getScheme() == null ? WEB_SOCKET_SCHEME : pushServerUri.getScheme();
    }

    private static String getHost(URI pushServerUri) {
        return pushServerUri.getHost() == null ? LOCAL_HOST : pushServerUri.getHost();
    }

    private static int getPort(URI pushServerUri, String scheme) {
        int port;
        if (pushServerUri.getPort() == -1) {
            if (WEB_SOCKET_SCHEME.equals(scheme)) {
                port = 80;
            } else if (WSS_SCHEME.equals(scheme)) {
                port = 443;
            } else {
                port = -1;
            }
        } else {
            port = pushServerUri.getPort();
        }
        return port;
    }

    //检查scheme是否是ws或wss
    private static void checkScheme(String scheme) {
        if (!WEB_SOCKET_SCHEME.equals(scheme) && !WSS_SCHEME.equals(scheme)) {
            logger.error("Only Support ws or wss scheme.");
            throw new RuntimeException("Only Support ws or wss scheme.");
        }
    }

    //如果WebSocket使用了SSL，也就是wss，那么初始化对应的sslContext
    private static void initSslContext(String scheme) throws Exception {
        boolean enableSSL = WSS_SCHEME.equals(scheme);
        if (enableSSL) {
            sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslContext = null;
        }
    }

    public void waitInputMessage(Channel channel) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            logger.info("Wait for input message.");
            String message = bufferedReader.readLine();
            if (INPUT_MESSAGE_QUIT.equals(message)) {
                break;
            } else if (INPUT_MESSAGE_CLOSE.equals(message)) {
                channel.writeAndFlush(new CloseWebSocketFrame());
                channel.closeFuture().sync();
                break;
            } else if (INPUT_MESSAGE_PING.equals(message)) {
                WebSocketFrame webSocketFrame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[]{8, 1, 8, 1}));
                channel.writeAndFlush(webSocketFrame);
            } else {
                WebSocketFrame webSocketFrame = new TextWebSocketFrame(message);
                channel.writeAndFlush(webSocketFrame);
            }
        }
    }

    public void shutdownGracefully() {
        eventLoopGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        uri = new URI(PUSH_SERVER_URI);
        String scheme = getScheme(uri);
        host = getHost(uri);
        port = getPort(uri, scheme);

        checkScheme(scheme);
        initSslContext(scheme);

        OperationNettyClient operationNettyClient = new OperationNettyClient();
        try {
            Channel channel = operationNettyClient.start();
            //运营客户端发送消息入口
            operationNettyClient.waitInputMessage(channel);
        } finally {
            operationNettyClient.shutdownGracefully();
        }
    }
}
