package com.bigshen.learningDemo.netty.NIO.buffer;

import lombok.val;

import java.nio.Buffer;
import java.nio.ByteBuffer;

/**
 * @author byj
 * @date 2025/3/20
 * @Description
 */
public class BufferDemo2 {
    public static void main(String[] args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(100);

        System.out.println("position=" + buffer.position()); // 0
        System.out.println("capacity=" + buffer.capacity()); // 100
        System.out.println("limit=" + buffer.limit()); // 100

        byte[] src = new byte[] {1, 2, 3, 4, 5};
        buffer.put(src);
        System.out.println("position=" + buffer.position());//position = 0~4，都填充了数据

        //此时position = 5，如果直接读取数据，是读不到的，会发现是空的、没有数据
        //所以需要手动操作一下position，调整到position = 0的位置，才能读到数据大小为5的字节数组src
        buffer.position(0);
        byte[] dst = new byte[5];
        buffer.get(dst);

        System.out.println("position=" + buffer.position());
        System.out.print("dst=[");

        for (int i = 0; i < dst.length; i++) {
            System.out.print(i);
            if (i < dst.length - 1) {
                System.out.print(",");
            }
        }
        System.out.print("]");

        // 还原缓冲区的状态：position设置为0、limit设置为capacity、丢弃mark。但是本质并不是删除数据，只是还原了那些标记位而已。
        // 还原之后就可以复用缓冲区里的空间，覆盖老的数据了。
        buffer.clear();

        // 准备读取刚写入的数据，就是将limit设置为当前position、将position设置为0、丢弃mark。
        //一般在写入完数据到缓冲区后，准备从位置=0开始读这段数据时，就可以使用flip。
        buffer.flip();

        // 将position设置为0、并且丢弃mark。
        //一般在读取了一遍数据后，还想要再次重新读取一遍数据时，就可以使用rewind，此时limit是不变的。
        buffer.rewind();
    }
}
