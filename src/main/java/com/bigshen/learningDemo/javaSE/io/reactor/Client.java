package com.bigshen.learningDemo.javaSE.io.reactor;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author byj
 * @date 2025/3/26
 * @Description
 */
public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 8954));
            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in, String.valueOf(StandardCharsets.UTF_8));

            while (true) {
                System.out.print("输入要发送的消息 (输入 'exit' 退出): ");
                String message = scanner.nextLine();

                if ("exit".equalsIgnoreCase(message)) {
                    System.out.println("客户端退出...");
                    break;
                }

                outputStream.write(message.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();

                byte[] buffer = new byte[1024];
                int read = socket.getInputStream().read(buffer);
                if (read != -1) {
                    System.out.println("收到服务器回复: " + new String(buffer, 0, read, StandardCharsets.UTF_8));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
