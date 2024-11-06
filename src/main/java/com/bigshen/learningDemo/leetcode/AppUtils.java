package com.bigshen.learningDemo.leetcode;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author byj
 * @date 2024/10/8
 * @Description
 */
public class AppUtils {

    /**
     * 检测端口是否重叠
     *
     * @param port1 8080-9090
     * @param port2 8070-8090
     * @return
     */
    public static boolean checkPortOverlap(String port1, String port2) {
        int[] range1 = parsePort(port1);
        int[] range2 = parsePort(port2);
        return range1[0] <= range2[1] && range2[0] <= range1[1];
    }

    public static int[] parsePort(String port) {
        String[] parts = port.split("-");
        int start = Integer.parseInt(parts[0]);
        int end = parts.length > 1 ? Integer.parseInt(parts[1]) : start;
        return new int[]{start, end};
    }


    /**
     * TCP正向代理资源
     * 检测传入的ip/域名/网段  是否和已有资源有重叠部分？
     *
     * @param newIp 传入的新ip
     * @param oldIp 已有的旧ip
     * @return 是否存在重复？ true：重复、false：不重复
     */
    public static boolean checkCidrOverlap(String newIp, String oldIp) {
        if (newIp.contains("/")) {
            String[] parts1 = newIp.split("/");
            InetAddress ip1 = parseIp(parts1[0]);
            int prefix1 = Integer.parseInt(parts1[1]);
            return checkOverlapWithCidr(ip1, prefix1, oldIp);
        } else {
            // TODO 排除域名、通配符域名的情况
            InetAddress ip1 = parseIp(newIp);
            return checkOverlapWithIp(ip1, oldIp);
        }
    }

    private static boolean checkOverlapWithCidr(InetAddress ip1, int prefix1, String cidr2) {
        String[] parts2 = cidr2.split("/");
        InetAddress ip2 = parseIp(parts2[0]);
        // 默认单个IP前缀为32
        int prefix2 = parts2.length > 1 ? Integer.parseInt(parts2[1]) : 32;

        return isOverlap(ip1, prefix1, ip2, prefix2);
    }

    private static boolean checkOverlapWithIp(InetAddress ip1, String cidr2) {
        return checkOverlapWithCidr(ip1, 32, cidr2); // 将单个IP视为/32网段
    }

    private static InetAddress parseIp(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException("无效的IP地址: " + ip);
        }
    }

    /**
     * 判断网段是否重叠
     *
     * @param ip1     第一个IP地址
     * @param prefix1 第一个IP地址的前缀长度（CIDR）
     * @param ip2     第二个IP地址
     * @param prefix2 第二个IP地址的前缀长度（CIDR
     * @return
     */
    private static boolean isOverlap(InetAddress ip1, int prefix1, InetAddress ip2, int prefix2) {
        // 获取IP地址的字节数组
        byte[] addr1 = ip1.getAddress();
        byte[] addr2 = ip2.getAddress();

        // 计算网络地址
        int mask1 = prefixToMask(prefix1);
        int mask2 = prefixToMask(prefix2);

        // 使用按位与操作 & 可以将 IP 地址按子网掩码取出其网络部分
        int net1 = bytesToInt(addr1) & mask1;
        int net2 = bytesToInt(addr2) & mask2;

        // 比较网络地址，判断是否网段重叠
        return net1 == net2 || (net1 & mask1) == (net2 & mask1) || (net2 & mask2) == (net1 & mask2);

    }

    /**
     * 根据前缀长度（CIDR）生成子网掩码
     *
     * @param prefix 前缀长度 (CIDR)
     * @return 子网掩码的int表示
     */
    private static int prefixToMask(int prefix) {
        if (prefix < 0 || prefix > 32) {
            throw new IllegalArgumentException("Prefix must be between 0 and 32");
        }
        return (prefix == 0) ? 0 : (-1 << (32 - prefix));
    }

    /**
     * 将字节数组转换为整数（将IP地址转换为其整数表示）
     *
     *  eg：192.168.1.1 对应的字节数组【192，168，1,1】
     *  其中：
     *  左移操作：x << 8 相当于 x乘2的8次方（即x乘256），
     *  按位或操作：| 对两个数字转为二进制，每一位进行比较，任意一位为 1 则结果该位为 1。


     *  0 << 8 | 192 = 192
     *  192 << 8 | 168 = 49320
     *  49320 << 8 | 1 = 12625921
     *  12625921 << 8 | 1 = 3232235521
     *
     *
     * @param bytes 字节数组 【192，168，1,1】
     * @return 整数 3232235521
     */
    private static int bytesToInt(byte[] bytes) {
        int result = 0;
        for (byte b : bytes) {
            result = (result << 8) | (b & 0xFF);
        }
        return result;
    }


    public static boolean checkOverlap(String domain1, String domain2) {
        // 处理通配符与通配符之间的重叠
        if (domain1.startsWith("*.") && domain2.startsWith("*.")) {
            return checkWildcardOverlap(domain1, domain2);
        }

        // 处理具体域名与通配符域名的重叠
        if (domain1.startsWith("*.")) {
            return checkSpecificWithWildcardOverlap(domain2, domain1);
        }

        if (domain2.startsWith("*.")) {
            return checkSpecificWithWildcardOverlap(domain1, domain2);
        }

        return false; // 两个都是具体域名，默认没有交集
    }

    private static boolean checkWildcardOverlap(String wildcard1, String wildcard2) {
        // 去掉 *.
        String suffix1 = wildcard1.substring(2);
        String suffix2 = wildcard2.substring(2);
        return suffix1.equals(suffix2) || isSubdomain(suffix1, suffix2) || isSubdomain(suffix2, suffix1);
    }

    private static boolean checkSpecificWithWildcardOverlap(String specificDomain, String wildcardDomain) {
        if (!wildcardDomain.startsWith("*.")) {
            return false; // 仅支持通配符域名
        }
        String suffix = wildcardDomain.substring(2); // 去掉 *.
        return specificDomain.endsWith(suffix);
    }

    private static boolean isSubdomain(String child, String parent) {
        return child.endsWith(parent);
    }


    /**
     * 1、新增的端口和已发布的TCP正向代理资源的端口进行对比
     * 2 新增的objectIp 判断类型
     * 2.1 是否为泛域名（包含*： *.koal.com） yes 只对比已发布的域名/泛域名类型
     * 2.2 是否为精准域名(不包含*，不是CIDR,不是IPV4类型：git.koal.com) yes 只对比已发布的域名/泛域名类型
     * 2.3 是否为CIDR网段（isValidCIDR方法：10.0.1.0/24）yes 只对比已发布的IP/CIDR网段类型
     * 2.4 是否为具体IP (isValidIPv4方法：10.0.1.25) yes 只对比已发布的IP/CIDR网段类型
     *
     *
     * @param args
     */
    public static void main(String[] args) {
        // 端口是否存在交集
//        System.out.println("端口范围是否有交集1: " + AppUtils.checkPortOverlap("1000-2000", "3000-4000")); // false
//        System.out.println("端口范围是否有交集2: " + AppUtils.checkPortOverlap("1-2000", "2000-3000")); // true
//        System.out.println("端口范围是否有交集3: " + AppUtils.checkPortOverlap("1000-2000", "1002")); // true
//        System.out.println("端口范围是否有交集4: " + AppUtils.checkPortOverlap("1000-2000", "1500-2500")); // true
//        System.out.println("端口范围是否有交集5: " + AppUtils.checkPortOverlap("600", "605-65535")); // false
//        System.out.println("端口范围是否有交集6: " + AppUtils.checkPortOverlap("601-65535", "601-65535")); // true

        // CIDR 网段是否有交集  具体ip会转为/32
        System.out.println("网段是否有交集1: " + checkCidrOverlap("10.0.247.210", "10.0.0.0/16")); // true
        System.out.println("网段是否有交集2: " + checkCidrOverlap("192.168.1.0/24", "192.168.1.128/25")); // true
        System.out.println("网段是否有交集3: " + checkCidrOverlap("192.168.1.0/24", "192.168.1.128")); // true
        System.out.println("网段是否有交集4: " + checkCidrOverlap("192.168.1.0/24", "192.168.2.0/24")); // false
        System.out.println("网段是否有交集5: " + checkCidrOverlap("10.0.247.0/24", "10.0.0.0/16")); // true
        System.out.println("网段是否有交集6: " + checkCidrOverlap("10.0.1.0/24", "10.0.0.0/16")); // true
        System.out.println("网段是否有交集6: " + checkCidrOverlap("10.0.1.0/24", "10.0.0.0/16")); // true
        System.out.println("网段是否有交集6: " + checkCidrOverlap("192.168.1.1", "192.168.1.2")); // false

        // 泛域名是否存在交集
//        System.out.println("是否存在交集: " + checkOverlap("*.g.koal.com", "*.koal.com")); // 应为 true
//        System.out.println("是否存在交集: " + checkOverlap("*.a.koal.com", "*.b.koal.com")); // 应为 false
//        System.out.println("是否存在交集: " + checkOverlap("a.koal.com", "*.koal.com")); // 应为 true
//        System.out.println("是否存在交集: " + checkOverlap("a.b.koal.com", "*.koal.com")); // 应为 true
//        System.out.println("是否存在交集: " + checkOverlap("a.koal.org", "*.koal.com")); // 应为 false

    }
}
