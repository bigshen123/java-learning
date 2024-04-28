package com.bigshen.learningDemo.javaSE.io.NIO.NIODemo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2020/1/3 13:27
 */
public class NIOClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8888);
        OutputStream out = socket.getOutputStream();
        String s = "hello world";
        out.write(s.getBytes());
        out.close();
    }
}
