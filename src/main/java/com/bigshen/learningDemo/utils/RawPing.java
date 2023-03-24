package com.bigshen.learningDemo.utils;

import java.io.IOException;
import java.net.*;

/**
 * @author byj
 * @date 2023/3/7
 */
public class RawPing {
    public static void main(String[] args) throws Exception {
        String sourceIpAddress = "10.0.210.202"; // 指定源 IP 地址
        String targetIpAddress = "10.0.210.204"; // 目标 IP 地址

        InetAddress sourceAddress = InetAddress.getByName(sourceIpAddress);
        InetAddress targetAddress = InetAddress.getByName(targetIpAddress);

        String command = "ping -S " + sourceAddress.getHostAddress() + " " + targetAddress.getHostAddress();
        Process process = Runtime.getRuntime().exec(command);
        process.waitFor();

        int exitValue = process.exitValue();
        if (exitValue == 0) {
            System.out.println("Ping succeeded");
        } else {
            System.out.println("Ping failed");
        }
    }
}
