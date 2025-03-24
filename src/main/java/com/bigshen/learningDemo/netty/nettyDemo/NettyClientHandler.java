package com.bigshen.learningDemo.netty.nettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author byj
 * @date 2025/3/19
 * @Description 说明一：
 * IO事件的处理类继承自ChannelInboundHandlerAdapter，客户端IO事件的处理类主要需要关注三个方法：channelActive()、channelRead()、exceptionCaught()。
 * 说明二：
 * 当客户端和服务端建立好TCP连接后，Netty的NIO线程会调用channelActive()方法，而调用ChannelHandlerContext的writeAndFlush()方法会将请求消息发送给服务端。
 * 说明三：
 * 当服务端返回应答消息时，channelRead()方法会被调用。当发生异常时，exceptionCaught()方法会被调用。
 * <p>
 * 注意：Netty里的数据是以ByteBuf为单位的，所有需要读和写的数据都必须放到一个ByteBuf中。其中通过ctx.alloc().buffer()可以分配一个ByteBuf，通过Unpooled.buffer()也可以分配一个ByteBuf。
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf requestBuffer;

    public NettyClientHandler() {
        byte[] requestBytes = "你好，我发送第一条消息".getBytes();
        requestBuffer = Unpooled.buffer(requestBytes.length);
        requestBuffer.writeBytes(requestBytes);
    }

    /**
     * 客户端和服务端链路建立成功后，就触发执行channelActive()方法循环发送100条消息。
     * 每发送一条消息就通过writeAndFlush()方法刷新一次，保证每条消息都被写入Channel。

     * 期望服务端应该会收到100条消息，但是实际上只收到了两条消息。其中一条包含"57条消息"，而另一条包含"43条消息"。
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
          ctx.writeAndFlush(requestBuffer);

//        for (int i = 0; i < 100; i++) {
//            ByteBuf buffer = ctx.alloc().buffer();
//            buffer.writeBytes("Hello".getBytes());
//            ctx.channel().writeAndFlush(buffer);
//        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf responseBuffer = (ByteBuf) msg;
        byte[] responseBytes = new byte[responseBuffer.readableBytes()];
        responseBuffer.readBytes(responseBytes);

        String response = new String(responseBytes, "UTF-8");
        System.out.println("接收到服务端的响应：" + response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


}
