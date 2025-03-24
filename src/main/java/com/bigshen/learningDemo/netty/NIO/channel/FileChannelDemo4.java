package com.bigshen.learningDemo.netty.NIO.channel;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author byj
 * @date 2025/3/20
 * @Description 从磁盘文件读取数据到Buffer缓冲区
 */
public class FileChannelDemo4 {
    public static void main(String[] args) throws Exception {
        FileInputStream in = new FileInputStream("D:\\NSAG\\test.txt");
        FileChannel channel = in.getChannel();

        ByteBuffer buffer = ByteBuffer.allocateDirect(5);
        channel.read(buffer);//读数据写入buffer，所以写完以后，buffer的position = 16

        buffer.flip();//position = 0，limit = 16
        byte[] data = new byte[5];
        buffer.get(data);

        System.out.println(new String(data));

        channel.close();
        in.close();
    }
}
