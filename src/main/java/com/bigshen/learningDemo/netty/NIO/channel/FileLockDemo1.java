package com.bigshen.learningDemo.netty.NIO.channel;

import java.io.FileInputStream;
import java.nio.channels.FileChannel;

/**
 * @author byj
 * @date 2025/3/20
 * @Description
 * JVM内的多个线程对同一个FileChannel进行写，是线程安全的。但如果是多个JVM对同一个FileChannel进行写，则不是线程安全。
 * 为此，FileChannel提供了文件锁功能，可以对文件上锁。FileChannel的文件锁分为共享锁和独占锁。
 */
public class FileLockDemo1 {
    public static void main(String[] args) throws Exception {
        //新建一个输入流
        FileInputStream in = new FileInputStream("D:\\NSAG\\test.txt");
        FileChannel channel = in.getChannel();

        //注意输入流是不能加独占锁的，否则会报错
        channel.lock(0, Integer.MAX_VALUE, true);//加共享锁
        Thread.sleep(60 * 60 * 1000);

        //强制数据从OS Cache刷入磁盘，避免停留在OS Cache中
//        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes());
//        channel.write(buffer);
//        channel.force(true);

        channel.close();
        in.close();
    }
}
