package com.bigshen.learningDemo.netty.BIO;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author byj
 * @date 2025/3/20
 * @Description
 */
public class SocketServer {
    public static void main(String[] args) throws Exception {
       // ServerSocket负责绑定IP地址、启动监听端口，Socket负责发起连接操作。在连接成功后，双方通过输入和输出流进行同步阻塞通信。
        ServerSocket serverSocket = new ServerSocket(9000);
        Socket socket = serverSocket.accept();//在这里会阻塞住，一直等待客户端建立连接

        //如果有个客户端要和当前服务端尝试建立TCP连接，并基于TCP协议来传输数据，发送一个一个的TCP包
        //那么客户端必须先和当前服务端的端口号建立连接
        //客户端Socket和服务端ServerSocket互相传递3次握手的TCP包，TCP连接就建立了

        //当客户端向当前服务端发起TCP三次握手建立一个连接后，这里就会构建出一个Socket
        //这个Socket就代表了当前服务端和某个客户端的一个TCP连接(Socket连接)

        //当建立了TCP连接后，客户端就会通过IO流的方式发送数据过来
        //由于IO流是无限的流，所以底层的TCP协议会把流式的数据拆分为一个一个的TCP包
        //然后将TCP包包裹在IP包里，IP包又会被包裹在以太网包里
        //最后通过底层的网络硬件设备以及以太网的协议，发送以太网包的数据

        //获取Socket的输入流
        InputStreamReader in = new InputStreamReader(socket.getInputStream());
        //获取Socket的输出流
        OutputStream out = socket.getOutputStream();

        char[] buf = new char[1024 * 1024];//JVM的一个缓冲数组
        int len = in.read(buf);

        //Socket的输入流，相当于不停读取客户端通过TCP协议发送过的一个一个的TCP包
        //然后把TCP包里的数据通过IO输入流的方式提供给服务端
        //这样服务端就可以通过IO输入流读取的方式，把TCP包里的数据读出来，然后放入JVM内存的一个缓冲数组中
        while (len != -1) {
            String request = new String(buf, 0, len);
            System.out.println("服务端接收到了请求：" + request);

            //输出流的意思是，服务端会通过IO流发送响应数据回客户端
            //此时在底层会把服务端的响应数据拆分为一个一个的TCP包，回传给客户端
            //这样客户端就可以接收到服务端发送的TCP包了
            out.write("收到，收到".getBytes());

            //in.read会阻塞在这里尝试读取数据，所以out.write要放在前面
            len = in.read(buf);

            //为什么需要通过while循环反复去读取Socket流传输过来的数据？
            //因为客户端是不停地用流的方式发送数据给服务端的，所以服务端需要不停地读取
            //此外，由于buf才1KB，如果客户端发送过来的数据是几十KB，
            //那么当服务端读取完1KB的数据后，还需要继续读取几十KB的数据，
            //因此才需要服务端不停的读取
        }

        //释放资源
        out.close();
        in.close();
        //通过TCP四次挥手断开连接
        socket.close();
        serverSocket.close();
    }
}
