package com.bigshen.learningDemo.network.udp.rudpPacket;

import java.nio.ByteBuffer;

/**
 * @author byj
 * @date 2025/4/23
 * @Description UDP实现可靠传输的demo
 * 定义数据包结构
 */

public class RudpPacket {
    public int seq;
    public int ack;
    public byte flag;
    public byte[] data;

    public static final byte FLAG_DATA = 0;
    public static final byte FLAG_ACK = 1;
    public static final byte FLAG_EOF = 2;

    public RudpPacket(int seq, int ack, byte flag, byte[] data) {
        this.seq = seq;
        this.ack = ack;
        this.flag = flag;
        this.data = data;
    }

    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(9 + data.length);
        buffer.putInt(seq);
        buffer.putInt(ack);
        buffer.put(flag);
        buffer.put(data);
        return buffer.array();
    }

    public static RudpPacket fromBytes(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int seq = buffer.getInt();
        int ack = buffer.getInt();
        byte flag = buffer.get();
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);
        return new RudpPacket(seq, ack, flag, data);
    }
}
