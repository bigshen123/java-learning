package com.bigshen.learningDemo.netty.websocket.push;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * @author byj
 * @date 2025/5/14
 * @Description 消息推送系统的Handler处理器 ping-pong探测
 */
public class NettyPushServerHandler extends SimpleChannelInboundHandler<Object> {

    private WebSocketServerHandshaker webSocketServerHandshaker;

    private static final Logger logger = LogManager.getLogger(NettyPushServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("Client Connection Established: " + ctx.channel());
        ChannelManager.add(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("Client Disconnected: " + ctx.channel());
        ChannelManager.remove(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame webSocketFrame) {
        //WebSocket网页客户端发送的是ping消息，它会不停的ping服务端，看看长连接是否存活和有效
        if (webSocketFrame instanceof PingWebSocketFrame) {
            logger.info("Receive ping frame from client: " + ctx.channel());
            WebSocketFrame pongWebSocketFrame = new PongWebSocketFrame(webSocketFrame.content().retain());
            ctx.channel().write(pongWebSocketFrame);
            return;
        }
        //WebSocket网页客户端发送一个请求过来，请求关闭这个WebSocket连接
        if (webSocketFrame instanceof CloseWebSocketFrame) {
            logger.info("Receive close WebSocket request from client: " + ctx.channel());
            webSocketServerHandshaker.close(ctx.channel(), ((CloseWebSocketFrame) webSocketFrame).retain());
            return;
        }
        //WebSocket网页客户端发送请求，但它不是text文本请求
        if (!(webSocketFrame instanceof TextWebSocketFrame)) {
            logger.error("Netty Push Server only support text frame, does not support other type frame.");
            String errorMsg = String.format("%s type frame is not supported.", webSocketFrame.getClass().getName());
            throw new UnsupportedOperationException(errorMsg);
        }

        //WebSocket网页客户端发送一个文本请求过来，是TextFrame类型的
        String request = ((TextWebSocketFrame) webSocketFrame).text();
        logger.info("Receive text frame[" + request + "] from client: " + ctx.channel());

        //构建响应
        TextWebSocketFrame response = new TextWebSocketFrame(request);
        //发送给所有连接，全连接推送
        ChannelManager.pushToAllChannels(response);
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))) {
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            sendHttpResponse(ctx, request, response);
            return;
        }
        logger.info("Receive handshake request from client: " + ctx.channel());

        //握手建立
        WebSocketServerHandshakerFactory factory = new WebSocketServerHandshakerFactory("ws://localhost:8998/push", null, false);
        webSocketServerHandshaker = factory.newHandshaker(request);
        if (webSocketServerHandshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            webSocketServerHandshaker.handshake(ctx.channel(), request);
            logger.info("Netty push server handshake with client: " + ctx.channel());
        }
    }

    /**
     * HTTP响应
     * @param ctx ctx
     * @param request request
     * @param response response
     */
    private void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, DefaultFullHttpResponse response) {
        if (response.status().code() != 200) {
            ByteBuf byteBuf = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
            response.content().writeBytes(byteBuf);
            logger.info("Http Response is not ok: " + byteBuf.toString(CharsetUtil.UTF_8));
            byteBuf.release();
        }
        ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
        if (response.status().code() != 200) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
