package com.bigshen.learningDemo.netty.nettyDemo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author byj
 * @date 2025/3/19
 * @Description
 */
public class NettyServer {
    public static void main(String[] args) {
        // 首先创建两个NioEventLoopGroup实例，bossGroup实例用于接收客户端的连接，workerGroup实例用于处理每个连接的读写。
        // NioEventLoopGroup是个线程组，它包含了一组NIO线程，专门用于处理网络事件。
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 然后创建ServerBootstrap实例，ServerBootstrap是Netty用于启动NIO服务端的启动引导类。
            ServerBootstrap b = new ServerBootstrap();//相当于Netty的服务器
            // 接着调用ServerBootstrap的group()方法指定线程模型，也就是将两个NIO线程组传递到ServerBootstrap中。
            b.group(bossGroup, workerGroup)//指定线程模型
                    // 然后调用ServerBootstrap的channel()方法指定IO模型为NIO，也就是指定创建的Channel为NioServerSocketChannel。
                    .channel(NioServerSocketChannel.class)//指定IO模型为NIO
                    // 然后调用ServerBootstrap的option()方法指定TCP参数，也就是配置NioServerSocketChannel的TCP参数。
                    .option(ChannelOption.SO_BACKLOG, 1024)//指定TCP参数
                    // 然后调用ServerBootstrap的 childHandler()方法指定业务处理逻辑，也就是绑定IO事件的处理类ChildHandler等。
                    .childHandler(new ChannelInitializer<NioSocketChannel>() {//指定IO处理逻辑
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new NettyServerHandler());//针对网络请求的处理逻辑
                        }
                    });
            // 完成服务端的辅助启动类的配置后，就调用它的bind()方法来异步绑定监听端口。
            // 然后继续调用它的sync()方法进行同步阻塞来等待绑定操作完成，绑定操作完成后会返回一个 ChannelFuture。
            ChannelFuture f = b.bind(50070).sync();//绑定端口，同步等待成功
            // 接着使用 ChannelFuture 的方法进行阻塞，直到服务端链路关闭。
            f.channel().closeFuture().sync();//等待服务端监听端口关闭
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 最后使用NIO线程组(NioEventLoopGroup)的shutdownGracefully()方法进行优雅退出。
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
