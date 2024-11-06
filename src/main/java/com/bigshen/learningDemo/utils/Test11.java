package com.bigshen.learningDemo.utils;

/**
 * @author byj
 * @date 2024/8/20
 * @Description
 */
public class Test11 {


    public static String extractAndFormatIpWithSubnet(String object) {
        // 域名模式资源跳过处理
        if (isDomainName(object)) {
            return object;
        }

        // 移除协议头（http://, https://, tcp://等）
        object = object.replaceAll("^(http://|https://|tcp://)", "");

        // 查找端口号和子网掩码的分隔符
        int colonIndex = object.indexOf(':');
        int slashIndex = object.indexOf('/');

        // 如果不存在子网掩码，返回 null
        if (slashIndex == -1) {
            return object;
        }
        String ipWithSubnet;
        // 存在子网掩码
        if (colonIndex == -1 || slashIndex < colonIndex) {
            if (colonIndex != -1) {
                // 提取子网掩码前部分，即去除端口号
                ipWithSubnet = object.substring(0, colonIndex);
            } else {
                // 没有端口号，直接返回IP加子网掩码部分
                ipWithSubnet = object;
            }
        } else {
            return object;
        }
        // 将结果套上方括号
        return "[" + ipWithSubnet + "]";
    }

    private static boolean isDomainName(String object) {
        // 去除协议头部分，例如 "http://", "https://"
        object = object.replaceFirst("^(http://|https://|tcp://)", "");

        // 去除端口部分，例如 ":80"
        if (object.contains(":")) {
            object = object.substring(0, object.indexOf(":"));
        }

        // 检查去除协议头和端口后的字符串是否包含字母，来判断是否为域名
        return object.matches(".*[a-zA-Z].*");
    }

    public static void main(String[] args) {
        String url1 = "http://10.0.1.9/24:8090";
        String url2 = "https://192.168.0.1/16:8080";
        String url3 = "tcp://172.16.0.1/28:9090";
        String url4 = "http://10.0.1.9:8090";
        String url5 = "http://10.0.1.9/24";
        String url6 = "http://an.koal.com";
        String url7 = "http://an.koal.com/24";
        String url8 = "http://*.koal.com/24:8090";

        System.out.println(extractAndFormatIpWithSubnet(url1)); // 输出: [10.0.1.9/24]
        System.out.println(extractAndFormatIpWithSubnet(url2)); // 输出: [192.168.0.1/16]
        System.out.println(extractAndFormatIpWithSubnet(url3)); // 输出: [172.16.0.1/28]
        System.out.println(extractAndFormatIpWithSubnet(url4)); // 输出: null
        System.out.println(extractAndFormatIpWithSubnet(url5));
        System.out.println(extractAndFormatIpWithSubnet(url6));
        System.out.println(extractAndFormatIpWithSubnet(url7));
        System.out.println(extractAndFormatIpWithSubnet(url8));
    }

}
