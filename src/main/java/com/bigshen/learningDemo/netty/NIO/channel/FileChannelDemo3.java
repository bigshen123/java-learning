package com.bigshen.learningDemo.netty.NIO.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author byj
 * @date 2025/3/20
 * @Description FileChannel是多线程并发安全的
 */
public class FileChannelDemo3 {
    @SuppressWarnings("resource")
    public static void main(String[] args) throws Exception {
        //构造一个传统的文件输出流
        FileOutputStream out = new FileOutputStream("D:\\NSAG\\test.txt");
        //通过文件输出流获取到对应的FileChannel，以NIO的方式来写文件
        final FileChannel channel = out.getChannel();

        //输出文件的内容是10个顺序的hello world
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes());
                    channel.write(buffer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }
        channel.close();
        out.close();
    }
}
