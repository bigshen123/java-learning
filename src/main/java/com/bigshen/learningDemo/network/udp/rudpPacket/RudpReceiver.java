package com.bigshen.learningDemo.network.udp.rudpPacket;

import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * @author byj
 * @date 2025/4/23
 * @Description 接收端
 */
public class RudpReceiver {
    private static final int PORT = 9999;
    private static final double LOSS_RATE = 0.2; // 20% simulated loss

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket(PORT);
        socket.setSoTimeout(10000); // 10秒无数据就退出
        Map<Integer, byte[]> receivedData = new TreeMap<>();
        Set<Integer> acked = new HashSet<>();

        byte[] buf = new byte[1024];
        DatagramPacket udpPacket = new DatagramPacket(buf, buf.length);

        boolean eofReceived = false;
        int lastSeq = Integer.MAX_VALUE;
        System.out.println("Receiver started.");

        while (true) {
            try {
                socket.receive(udpPacket);
            } catch (SocketTimeoutException e) {
                if (eofReceived) break;
                else continue;
            }

            if (Math.random() < LOSS_RATE) {
                System.out.println("Simulated packet loss.");
                continue;
            }

            RudpPacket packet = RudpPacket.fromBytes(Arrays.copyOf(buf, udpPacket.getLength()));
            System.out.println("Received packet seq: " + packet.seq + ", flag: " + packet.flag);

            if (packet.flag == RudpPacket.FLAG_EOF) {
                lastSeq = packet.seq;
                eofReceived = true;
            } else if (packet.flag == RudpPacket.FLAG_DATA) {
                receivedData.putIfAbsent(packet.seq, packet.data);
            }

            RudpPacket ack = new RudpPacket(0, packet.seq, RudpPacket.FLAG_ACK, new byte[0]);
            byte[] ackBytes = ack.toBytes();
            DatagramPacket ackUdp = new DatagramPacket(
                    ackBytes, ackBytes.length, udpPacket.getAddress(), udpPacket.getPort());
            socket.send(ackUdp);
        }

        FileOutputStream fos = new FileOutputStream("output.txt");
        for (Map.Entry<Integer, byte[]> entry : receivedData.entrySet()) {
            fos.write(entry.getValue());
        }
        fos.close();
        socket.close();
        System.out.println("File received with " + receivedData.size() + " packets.");
    }
}
