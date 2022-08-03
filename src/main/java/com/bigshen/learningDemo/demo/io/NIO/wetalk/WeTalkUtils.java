package com.bigshen.learningDemo.demo.io.NIO.wetalk;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description:
 * @Author: BIGSHEN
 * @Date: 2019/12/28 18:08
 */
public class WeTalkUtils {
    private static final int BUFFER_SIZE = 128;

    /**
     * 发送消息
     *
     * @param channel
     * @param msg
     * @throws IOException
     */
    public static void sendMsg(SocketChannel channel, String msg) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.put(msg.getBytes());
        buffer.flip();
        channel.write(buffer);
    }

    /**
     * 回复消息
     *
     * @param channel
     * @return
     * @throws IOException
     */
    public static String recvMsg(SocketChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        channel.read(buffer);

        buffer.flip();
        byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return new String(bytes);
    }

}
