package com.bigshen.learningDemo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author byj
 * @date 2024/8/20
 * @Description
 */
public class Test12 {

    public static String extractAndFormatPort(String url) {
        // 匹配带端口的URL格式，包括端口范围和通配符 *
        Pattern portPattern = Pattern.compile(":(\\*|\\d{1,5}(-\\d{1,5})?)");
        Matcher matcher = portPattern.matcher(url);

        if (matcher.find()) {
            String portPart = matcher.group(1);

            // 检查是否是通配符 *
            if ("*".equals(portPart)) {
                return "0~65535";
            }

            // 检查是否是端口范围
            if (portPart.contains("-")) {
                // 将端口范围 8090-9090 转为 8090~9090
                return portPart.replace("-", "~");
            }

            // 直接返回匹配到的单个端口
            return portPart;
        }
        return null;
    }

    public static void main(String[] args) {
        String object = "http://10.0.1.9:80";
        String[] split = object.split("://");
        String ipAndPortPart = split[1];
        if (ipAndPortPart.contains(":")) {
            String[] ipAndPort = ipAndPortPart.split(":");
            System.out.println("IP: " + ipAndPort[0]);
            System.out.println("Port: " + ipAndPort[1]);
        } else {
            System.out.println("IP: " + ipAndPortPart);
            System.out.println("No port provided");
        }
//        System.out.println(extractAndFormatPort("http://10.0.1.9:8090"));          // 输出: 8090
//        System.out.println(extractAndFormatPort("http://10.0.1.9:8090-9090"));     // 输出: 8090~9090
//        System.out.println(extractAndFormatPort("http://10.0.1.9:*"));             // 输出: 0~65535
//        System.out.println(extractAndFormatPort("http://10.0.1.9/24:8090-9090"));             // 输出: 8090~9090
    }
}
