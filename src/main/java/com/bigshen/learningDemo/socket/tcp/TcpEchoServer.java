package com.bigshen.learningDemo.socket.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author byj
 * @date 2024/2/6
 * @Description
 */
public class TcpEchoServer {
    // serverSocket 是场外拉客的男销售
    // clientSocket 就是内场说明楼盘情况的专业顾问
    // serverSocket 只有一个，clientSocket 会给每个客户端都分配一个
    private ServerSocket serverSocket = null;

    public TcpEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        // 创建线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        System.out.println("服务器启动！");
        while (true) {
            Socket clientSocket = serverSocket.accept();

            // 往线程池里添加任务
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        processConnection(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    // 通过这个 processConnection方法 来处理这个连接
    // 读取请求
    // 根据请求计算响应
    // 把响应返回给客户端
    private void processConnection(Socket clientSocket) throws IOException {
        System.out.printf("地址与端口为[%s:%d] 的客户端上线！",clientSocket.getInetAddress().toString(),
                clientSocket.getPort());

        // try() 这种写法， ()中允许写多个流对象， 使用 ; 来分割
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {
            //为了方便，我们把字节流包装成字符流
            Scanner scanner = new Scanner(inputStream);
            PrintWriter printWriter = new PrintWriter(outputStream);

            while (true) {
                // 1.读取请求
                if (! scanner.hasNext()) {
                    // 读取的流到了结尾了(对端关闭了)
                    System.out.printf("地址与端口为[%s:%d] 的客户端下线！",clientSocket.getInetAddress().toString(),
                            clientSocket.getPort());
                    break;
                }
                // 直接使用 scanner 读取一段字符串
                String request = scanner.next();

                // 2.根据请求计算响应
                String response = process(request);

                // 3.把响应协会给客户端，不要忘了，相应里也是要带上换行的
                printWriter.println(response);
                // 刷新缓冲区
                printWriter.flush();
                System.out.printf("客户端地址与端口为[%s:%d]  req:%s;  resp:%s",clientSocket.getInetAddress().toString(),
                        clientSocket.getPort(), request, response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //最后的最后，一定要显式地关闭文件！！！！！
            clientSocket.close();
        }
    }

    private String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoServer tcpEchoServer = new TcpEchoServer(9090);
        tcpEchoServer.start();
    }
}
