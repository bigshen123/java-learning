package com.bigshen.learningDemo.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * @author byj
 * @date 2024/8/22
 * @Description
 */
public class IpUtils {


    private static final String regex = "^[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?(\\.[a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?)*$";
    private static final String IPV4_PART = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";
    private static final String IPV4_REGEX = IPV4_PART + "\\." + IPV4_PART + "\\." + IPV4_PART + "\\." + IPV4_PART;
    private static final String CIDR_REGEX = IPV4_REGEX + "/([0-9]|[1-2][0-9]|3[0-2])";
    /**
     * 用于校验IPv4地址
     */
    private static final Pattern IPV4_PATTERN = Pattern.compile("^" + IPV4_REGEX + "$");
    /**
     * 用于校验CIDR地址
     */
    private static final Pattern CIDR_PATTERN = Pattern.compile("^" + CIDR_REGEX + "$");

    /**
     * 排除协议和端口 只保留ip部分（兼容带子网掩码的情况）
     * eg：http://10.0.1.9/24:8090
     * http://10.0.1.9:8090
     *
     * @param object object
     * @return ip
     */
    public static String extractPort(String object) {
        object = object.replaceAll("^(http://|https://|tcp://|cidr://)", "");
        int colonIndex = object.indexOf(':');
        if (colonIndex != -1 && colonIndex < object.length() - 1) {
            String potentialPort = object.substring(colonIndex + 1);
            int slashIndex = potentialPort.indexOf('/');
            if (slashIndex != -1) {
                return potentialPort.substring(0, slashIndex);
            }
            return potentialPort;
        }
        return null;
    }

    /**
     * 校验ipv4是否合法
     *
     * @param object ip支持两种：
     *               正常ipv4格式：10.0.1.8
     *               CIDR格式： 10.0.1.8/24
     * @return
     */
    public static boolean isValidIp(String object) {
        String ip = extractIp(object);
        // 域名则不做校验
        if (isDomainName(object)) {
            return true;
        }
        return isValidIPv4(ip) || isValidCIDR(ip);
    }

    private static boolean isIPAddress(String input) {
        try {
            // 如果能成功解析成IP地址，则返回true
            InetAddress.getByName(input);
            return true;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * 排除协议和端口 只保留ip部分（兼容带子网掩码的情况）
     * eg：http://10.0.1.9/24:8090
     * http://10.0.1.9:8090
     *
     * @param object object
     * @return ip
     */
    public static String extractIp(String object) {
        object = object.replaceAll("^(http://|https://|tcp://|cidr://)", "");
        int colonIndex = object.indexOf(':');
        if (colonIndex != -1) {
            // 去除端口部分
            return object.substring(0, colonIndex);
        }
        return object;
    }

    /**
     * 检查输入的资源对象是否为域名模式
     *
     * @param object 资源对象 eg：http://test.koal.com:12345
     * @return true/false
     */
    private static boolean isDomainName(String object) {
        // 去除协议头部分，例如 "http://", "https://"
        object = object.replaceFirst("^(http://|https://|tcp://|cidr://)", "");

        // 去除端口部分，例如 ":80"
        if (object.contains(":")) {
            object = object.substring(0, object.indexOf(":"));
        }

        // 检查去除协议头和端口后的字符串是否包含字母，来判断是否为域名
        return object.matches(".*[a-zA-Z].*");
    }

    /**
     * 校验是否为IPV4格式地址
     *
     * @param ip ip地址
     * @return true/false
     */
    public static boolean isValidIPv4(String ip) {
        return IPV4_PATTERN.matcher(ip).matches();
    }

    /**
     * 校验是否为CIDR格式IP
     *
     * @param ip ip地址
     * @return true/false
     */
    public static boolean isValidCIDR(String ip) {
        return CIDR_PATTERN.matcher(ip).matches();
    }

    /**
     * 将object资源地址端口 转为trp需要的格式
     *
     * @param object eg：http://10.0.1.8/24:8090-9090 ------> 8090~9090
     *               http://10.0.1.8/24:* ------> 0~65535
     * @return 特定的端口格式 eg：8090~9090
     */
    public static String extractAndFormatPort(String object) {
        // 域名模式不特殊处理
        if (isDomainName(object)) {
            return null;
        }
        // 去除协议
        object = object.replaceAll("^(http://|https://|tcp://|cidr://)", "");

        // 不包含端口部分则不做处理
        if (!object.contains(":")) {
            return null;
        }
        // 获取端口部分
        String portPart = object.substring(object.indexOf(":") + 1);
        if ("*".equals(portPart)) {
            return "0~65535";
        }
        // 将端口范围 eg：8090-9090 转为 8090~9090
        if (portPart.contains("-")) {
            return portPart.replace("-", "~");
        }
        return null;
    }
    public static boolean containsWildcard(String[] domainArray) {
        // 使用stream检查数组中是否有包含'*'的字符串
        return Arrays.stream(domainArray)
                .anyMatch(domain -> domain.contains("*"));
    }
    public static void main(String[] args) {
//        String ip = extractIp("https://127.0.0.1:8090");
//        String ip2 = extractIp("https://127.0.0.1");
//        String ip3 = extractIp("127.0.0.1");
//        System.out.println(ip);
//        System.out.println(ip2);
//        System.out.println(ip3);

//        boolean ipAddress1 = isIPAddress("127.0.0.1");
//        System.out.println(ipAddress1);
//        boolean ipAddress2 = isIPAddress("127.0.1.0/24");
//        System.out.println(ipAddress2);
//        boolean ipAddress3 = isIPAddress("test.koal.com");
//        System.out.println(ipAddress3);
//        boolean ipAddress4 = isIPAddress("*.koal.com");
//        System.out.println(ipAddress4);

//        System.out.println(containsWildcard(new String[]{""}));
//        System.out.println(containsWildcard(new String[]{"test.koal.com","test.*.koal.com"}));
//        System.out.println(containsWildcard(new String[]{"*.koal.com"}));
//        System.out.println(containsWildcard(new String[]{null}));

//        String domain = "example.com";
//        String domain1 = "123.example.com";
//        String domain2 = "10.0.1.8";
//        String domain3 = "10.0.1.8/24";
//
//        System.out.println(domain.matches(regex));
//        System.out.println(domain1.matches(regex));
//        System.out.println(domain2.matches(regex));
//        System.out.println(domain3.matches(regex));

        System.out.println(isValidCIDR("10.0.1.2"));

    }


}
