package com.bigshen.learningDemo.netty.NIO.channel;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * @author byj
 * @date 2025/3/20
 * @Description 由于该程序对文件加的是独占锁，所以此时会报错，只能加共享锁。
 */
public class FileLockDemo2 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile file = new RandomAccessFile("D:\\NSAG\\test.txt", "rw");
        FileChannel channel = file.getChannel();

        //加独占锁，阻塞住，会等待别人释放共享锁了，这里才能成功加独占锁
        channel.lock(0, Integer.MAX_VALUE, false);
        System.out.println("加锁成功");
        Thread.sleep(60 * 60 * 1000);

        channel.close();
        file.close();
    }
}
