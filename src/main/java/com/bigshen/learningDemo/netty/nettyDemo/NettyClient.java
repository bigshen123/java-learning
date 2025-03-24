package com.bigshen.learningDemo.netty.nettyDemo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author byj
 * @date 2025/3/19
 * @Description
 */
public class NettyClient {
    public static void main(String[] args) {
        // 首先创建客户端处理IO读写的NioEventLoopGroup实例。NioEventLoopGroup是个线程组，它包含了一组NIO线程，专门用于处理网络事件。
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 然后创建Bootstrap实例，Bootstrap是Netty用于启动NIO客户端的启动引导类。
            Bootstrap bootstrap = new Bootstrap();
            // 接着调用Bootstrap的group()、channel()、option()、handler()方法配置好：线程模型、IO模型、TCP参数、业务处理逻辑。
            bootstrap.group(workerGroup)//指定线程模型
                    .channel(NioSocketChannel.class)//指定IO模型为NIO
                    .option(ChannelOption.TCP_NODELAY, true)//指定TCP参数
                    .handler(new ChannelInitializer<Channel>() {//指定IO处理逻辑
                        @Override
                        protected void initChannel(Channel channel) throws Exception {
                            channel.pipeline().addLast(new NettyClientHandler());
                        }
                    });
            // 完成客户端启动辅助类的配置后，就调用它的connect()方法来异步发起连接，然后调用sync()方法进行同步阻塞来等待连接成功。
            ChannelFuture f = bootstrap.connect("127.0.0.1", 50070).sync();//建立连接
            // 接着使用ChannelFuture的方法进行阻塞，直到客户端连接关闭。
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 最后使用NIO线程组(NioEventLoopGroup)的shutdownGracefully()方法进行优雅退出。
            workerGroup.shutdownGracefully();
        }
    }
}
