package com.bigshen.learningDemo.netty.NIO.buffer;

import java.nio.ByteBuffer;

/**
 * @author byj
 * @date 2023/10/31
 * Bytebuffer 读写底层原理 数据读写主要采用三个参数来控制
 * <p>
 * 核心思想：Bytebuffer的读写共用position、limit参数，因此需要切换至读模式(调用flip())和写模式(调用)
 */
public class BufferDemo {
    public static void main(String[] args) throws Exception {
        byte[] data = new byte[]{55, 56, 57, 58, 59};
        ByteBuffer buffer = ByteBuffer.wrap(data);

        System.out.println(buffer.capacity()); // 5
        System.out.println(buffer.position()); // 0
        System.out.println(buffer.limit()); // 5

        System.out.println(buffer.get());//把当前position所在位置的数据读取一位出来 55
        System.out.println(buffer.position()); // 1
        buffer.mark();//在position = 1的时候打的mark，标记

        //buffer.position(3);
        //buffer.limit(4);

        buffer.position(3);
        System.out.println(buffer.get()); // 58
        System.out.println(buffer.position()); // 4

        buffer.reset(); // 跳到上次标记的地方
        System.out.println(buffer.position()); // 1


        //适用于从磁盘文件读数据出来，或者从网络里读数据进来
        ByteBuffer buffer1 = ByteBuffer.allocate(10);
        //如果用direct模式分配Buffer，整体性能可以比普通模式稍微高些
        ByteBuffer buffer2 = ByteBuffer.allocateDirect(10);


    }
}
