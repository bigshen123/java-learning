package com.bigshen.learningDemo.socket.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author byj
 * @date 2024/2/6
 * @Description
 */
public class UdpEchoServer {
    // 需要定义一个 socket对象，
    // 通过网络通信，必须要使用 socket对象
    private DatagramSocket socket = null;

    // 绑定一个端口，不一定能成功：
    // 如果某个端口已经被其他进程占用了，此时的绑定操作就会出错，
    // 因为同一个主机上，一个端口，同一时刻，只能被一个进程绑定
    public UdpEchoServer(int port) throws SocketException {
        //构造 socket 的同时，要指定 关联/绑定 的端口
        socket = new DatagramSocket(port);
    }

    // 启动服务器的主要逻辑
    public void start() throws IOException {
        System.out.println("服务器启动！");
        while (true) {
            // 每次循环，要做 3 件事情：
            // 1. 读取请求并解析
            //先构造一个 请求数据包
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096], 4096);
            // 从网卡上接收请求，然后放在这个数据包里
            socket.receive(requestPacket);
            // 为了方便处理这个请求，把数据包转成 String
            String request = new String(requestPacket.getData(), 0, requestPacket.getLength());

            // 2. 根据请求计算响应（由于我们做的是回显服务器，因此省略这个步骤）
            String response = process(request);

            // 3. 把响应结果写到客户端
            // 根据 response字符串，构造一个 DatagramPacket
            // 和请求 packet 不同，此处构造响应的时候，要指定这个包发送给谁
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), response.getBytes().length,
                    requestPacket.getSocketAddress());
            //requestPacket 是从客户端里收来的，getSocketAddress 就会得到客户端的 IP 和 端口

            //发送响应
            socket.send(responsePacket);

            //打印一些数据，以便观察结果
            System.out.printf("请求的地址和端口号:[%s:%d]  请求req:%s,  响应resp:%s\n",
                    requestPacket.getAddress().toString(), responsePacket.getPort(),
                    request, response);
        }
    }

    // 这个方法是通过请求计算响应，是服务器的重要环节
    // 由于我们写的是一个 回显服务器，请求是什么，响应就是什么
    // 如果我们后面写一个别的功能的服务器，就可以修改这个 process方法，根据请求来重新构造响应
    public String process(String request) {
        return request;
    }

    //主函数：在主函数里启动服务器
    public static void main(String[] args) throws IOException {
        //要传入端口号
        UdpEchoServer udpEchoServer = new UdpEchoServer(9090);

        //启动服务器
        udpEchoServer.start();
    }
}
