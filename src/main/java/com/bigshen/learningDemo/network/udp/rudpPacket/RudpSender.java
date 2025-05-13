package com.bigshen.learningDemo.network.udp.rudpPacket;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;

/**
 * @author byj
 * @date 2025/4/23
 * @Description 发送端
 */
public class RudpSender {
    private static final int TIMEOUT_MS = 1000;
    private static final int PACKET_SIZE = 512;
    private static final int PORT = 9999;

    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress receiverAddress = InetAddress.getByName("127.0.0.1");

        File file = new File("D:\\test.txt");
        byte[] fileData = new byte[(int) file.length()];
        new FileInputStream(file).read(fileData);

        int seq = 1;
        for (int i = 0; i < fileData.length; i += PACKET_SIZE) {
            int end = Math.min(i + PACKET_SIZE, fileData.length);
            byte[] chunk = Arrays.copyOfRange(fileData, i, end);
            RudpPacket packet = new RudpPacket(seq, 0, RudpPacket.FLAG_DATA, chunk);
            System.out.println("Sending packet seq: " + seq);
            sendWithRetry(socket, receiverAddress, packet);
            seq++;
        }

        RudpPacket eof = new RudpPacket(seq, 0, RudpPacket.FLAG_EOF, new byte[0]);
        System.out.println("Sending EOF seq: " + seq);
        sendWithRetry(socket, receiverAddress, eof);
        socket.close();
        System.out.println("File sent.");
    }

    private static void sendWithRetry(DatagramSocket socket, InetAddress addr, RudpPacket packet) throws IOException {
        byte[] data = packet.toBytes();
        DatagramPacket udpPacket = new DatagramPacket(data, data.length, addr, PORT);
        socket.setSoTimeout(TIMEOUT_MS);

        byte[] ackBuf = new byte[1024];
        DatagramPacket ackPacket = new DatagramPacket(ackBuf, ackBuf.length);

        while (true) {
            socket.send(udpPacket);
            try {
                socket.receive(ackPacket);
                RudpPacket ack = RudpPacket.fromBytes(Arrays.copyOf(ackBuf, ackPacket.getLength()));
                if (ack.flag == RudpPacket.FLAG_ACK && ack.ack == packet.seq) {
                    return;
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Timeout for seq: " + packet.seq + ", retrying...");
            }
        }
    }
}
