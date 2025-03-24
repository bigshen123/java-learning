package com.bigshen.learningDemo.netty.BIO;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author byj
 * @date 2025/3/20
 * @Description
 */
public class SocketClient {
    public static void main(String[] args) throws Exception {
        //此处应该是会找DNS服务查找域名对应的IP地址
        Socket socket = new Socket("localhost", 9000);

        //接下来需要跟某个IP地址上的9000端口的服务器程序进行TCP三次握手，然后建立连接
        //这时就会构造一个三次握手中的第一次握手的TCP包，在这个TCP包里放入三次握手需要的数据
        //然后把这个TCP包封装在IP包里，IP包里是有对应的目标的IP地址，IP包再封装在以太网包里
        //接着通过底层的硬件设备和以太网协议把以太网包发送出去
        //经过路由器时，会通过IP地址查找路由表来确定下一个路由器的位置
        //查找到下一个路由器的mac地址后将其写入到以太网包头，继续通过下一个子网广播出去
        //通过这种方式层层转发，一直到对应的服务器上去

        //服务端接收到三次握手的第一次握手的TCP包后
        //服务端就会回传第二次握手的TCP包给这个客户端的程序，客户端会再次发送第三次握手的TCP包过去
        //这样三次握手成功，TCP连接建立起来了
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        OutputStream out = socket.getOutputStream();

        //发送数据流，底层拆分为一个一个的TCP包发到服务端去
        out.write("你好".getBytes());

        char[] buf = new char[1024 * 1024];
        int len = in.read(buf);

        while (len != -1) {
            String response = new String(buf, 0, len);
            System.out.println("客户端接收到了响应：" + response);
            len = in.read(buf);
        }

        //释放资源
        in.close();
        out.close();
        //通过TCP四次挥手断开连接
        socket.close();
    }
}
