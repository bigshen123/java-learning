package com.bigshen.learningDemo.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author byj
 * @date 2025/3/21
 * @Description
 */

public class NettyWebSocketServerHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LogManager.getLogger(NettyWebSocketServerHandler.class);
    private static ChannelGroup webSocketClients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //WebSocket网页代码里发送过来的数据
        String request = msg.text();
        logger.info("Netty Server receives request: " + request + ".");
        TextWebSocketFrame response = new TextWebSocketFrame("Hello, I am Netty Server.");
        ctx.writeAndFlush(response);
    }

    //如果一个网页WebSocket客户端跟Netty Server建立了连接之后，会触发这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        webSocketClients.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        logger.info("websocket client is closed, channel id: " + ctx.channel().id().asLongText() + "[" + ctx.channel().id().asShortText() + "]");
    }
}