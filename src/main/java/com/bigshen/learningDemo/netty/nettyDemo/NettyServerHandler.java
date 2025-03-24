package com.bigshen.learningDemo.netty.nettyDemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author byj
 * @date 2025/3/19
 * @Description
 *
 * 说明一：
 * IO事件的处理类继承自ChannelInboundHandlerAdapter，服务端IO事件的处理类主要需要关注三个方法：
 * channelRead()、channelReadComplete()、exceptionCaught()。

 * 说明二：
 * ByteBuf类似于NIO的ByteBuffer，但功能更强大和灵活。通过ByteBuf的 readableBytes()方法可以获得缓冲区可读的字节数，
 * 然后就可以根据缓冲区可读的字节数创建byte数组，接着通过ByteBuf的readBytes()方法便可以将缓冲区的字节数组复制到新创建的byte数组中。

 * 说明三：
 * 通过 ChannelHandlerContext 的write()方法会把待发送的消息放到发送缓冲区中，
 * 通过ChannelHandlerContext的flush()方法会将发送缓冲区中的消息写入到SocketChannel中发送出去。

 * 为了防止频繁唤醒Selector进行消息发送，ChannelHandlerContext的write()方法并不直接将消息写入SocketChannel中，而只是把消息放到发送缓冲区中。
 * 当调用ChannelHandlerContext的flush()方法时，才会将发送缓冲区中的消息写入到SocketChannel中发送出去。
 */

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("服务端读到数据" + byteBuf.toString());
        ByteBuf requestBuffer = (ByteBuf) msg;
        byte[] requestBytes = new byte[requestBuffer.readableBytes()];
        requestBuffer.readBytes(requestBytes);

        String request = new String(requestBytes, "UTF-8");
        System.out.println("接收到的请求：" + request);

        String response = "你好，我收到你的消息了";
        ByteBuf responseBuffer = Unpooled.copiedBuffer(response.getBytes());
        ctx.write(responseBuffer);

        //Netty底层就有类似Processor的东西，负责从网络连接中读取请求
        //然后把读取出来的请求交给这里的Handler来处理，处理完以后把响应返回回去
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
