package com.bigshen.learningDemo.netty.websocket.push;

import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author byj
 * @date 2025/5/14
 * @Description 运营客户端的Handler处理器
 */
public class OperationNettyClientHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LogManager.getLogger(OperationNettyClientHandler.class);
    private final WebSocketClientHandshaker webSocketClientHandshaker;
    private ChannelFuture channelFuture;

    public OperationNettyClientHandler(WebSocketClientHandshaker webSocketClientHandshaker) {
        this.webSocketClientHandshaker = webSocketClientHandshaker;
    }

    public ChannelFuture channelFuture() {
        return channelFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        channelFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        webSocketClientHandshaker.handshake(ctx.channel());
        logger.info("Operation Netty Client send WebSocket handshake request.");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        logger.info("netty client disconnected.");
    }

    protected void channelRead0(ChannelHandlerContext ctx, Object msg) {
        Channel channel = ctx.channel();
        if (!webSocketClientHandshaker.isHandshakeComplete()) {
            try {
                webSocketClientHandshaker.finishHandshake(channel, (FullHttpResponse) msg);
                logger.info("Netty Client connected.");
                ((ChannelPromise) channelFuture).setSuccess();
            } catch (WebSocketHandshakeException e) {
                logger.error("WebSocket handshake failed.", e);
                ((ChannelPromise) channelFuture).setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException("Not Supported HTTP Response.");
        }

        WebSocketFrame webSocketFrame = (WebSocketFrame) msg;
        if (webSocketFrame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame) webSocketFrame;
            logger.info("Receives text frame: " + textWebSocketFrame.text());
        } else if (webSocketFrame instanceof PongWebSocketFrame) {
            logger.info("Receives pong frame: " + webSocketFrame);
        } else if (webSocketFrame instanceof CloseWebSocketFrame) {
            logger.info("Receives close WebSocket frame, Netty Client is closing.");
            channel.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("Operation Netty client handler exception caught.", cause);
        if (!channelFuture.isDone()) {
            ((ChannelPromise) channelFuture).setFailure(cause);
        }
        ctx.close();
    }
}
