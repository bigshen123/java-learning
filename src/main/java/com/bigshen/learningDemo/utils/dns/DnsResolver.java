package com.bigshen.learningDemo.utils.dns;

import org.xbill.DNS.*;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author byj
 * @date 2024/9/9
 * @Description
 */
public class DnsResolver {
    private static final String DNS_SERVER = "127.0.0.1";
    private static final int DNS_PORT = 53;

    private static final int BUFFER_SIZE = 512;

    // 创建DNS请求的二进制数据
    public static byte[] createDnsQuery(String domain) throws Exception {
        Name name = Name.fromString(domain, Name.root);
        // A记录：IPv4地址  IN: Internet
        Record record = Record.newRecord(name, Type.A, DClass.IN);
        Message query = Message.newQuery(record);
        return query.toWire();
    }

    // 通过UDP发送请求并接收响应
    public static byte[] sendDnsQuery(byte[] query) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName(DNS_SERVER);

        DatagramPacket packet = new DatagramPacket(query, query.length, serverAddress, DNS_PORT);
        socket.send(packet);

        // DNS响应通常较小
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length);
        socket.receive(responsePacket);

        socket.close();
        return responsePacket.getData();
    }

    // 合并 query 和 response 的数据
    public static byte[] mergeQueryAndResponse(byte[] query, byte[] response) {
        // 创建一个新数组，用于存储 query 和 response 的合并数据
        // 额外的8字节存储长度信息
        byte[] merged = new byte[query.length + response.length + 8];

        // 将 query 的长度存入前 4 字节
        System.arraycopy(intToBytes(query.length), 0, merged, 0, 4);

        // 将 response 的长度存入接下来的 4 字节
        System.arraycopy(intToBytes(response.length), 0, merged, 4, 4);

        // 复制 query 数据到新数组中
        System.arraycopy(query, 0, merged, 8, query.length);

        // 复制 response 数据到新数组中
        System.arraycopy(response, 0, merged, 8 + query.length, response.length);

        return merged;
    }
    // 将 int 转换为字节数组 (大端序)
    public static byte[] intToBytes(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value
        };
    }

    // 从DNS响应中解析IP地址
    public static String parseDnsResponse(byte[] response) throws Exception {
        Message message = new Message(response);
        Record[] answers = message.getSectionArray(Section.ANSWER);

        for (Record record : answers) {
            if (record instanceof ARecord) {
                return ((ARecord) record).getAddress().getHostAddress();
            }
        }
        return null;
    }

    // 业务接口实现
    public static byte[] resolveBinary(String domain) throws Exception {
        // 1. 构造DNS请求
        byte[] query = createDnsQuery(domain);

        // 2. 发送请求并获取响应
        byte[] response = sendDnsQuery(query);

        // 3. 返回原始的DNS请求和响应
        // 或者根据需求返回 query 和 response 的合并数据
        return mergeQueryAndResponse(query, response);
    }

    public static void main(String[] args) throws Exception {
        // 示例调用
        String domain = "git.koal.com";
        byte[] response = resolveBinary(domain);
        System.out.println("Resolved IP: " + parseDnsResponse(response));
    }
}
