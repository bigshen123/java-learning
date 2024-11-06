package com.bigshen.learningDemo.utils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.SubnetUtils;


/**
 * @author byj
 * @date 2024/8/19
 * @Description
 */
public class IpSubnetValidator {

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

    private static final String PORT_REGEX = "([0-9]{1,4}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])";
    private static final String PORT_RANGE_REGEX = PORT_REGEX + "(-" + PORT_REGEX + ")?";
    private static final Pattern PORT_PATTERN = Pattern.compile("^" + PORT_REGEX + "$");
    private static final Pattern PORT_RANGE_PATTERN = Pattern.compile("^" + PORT_RANGE_REGEX + "$");

//    public static void main(String[] args) {
//        try {
//            String subnet = "192.168.1.0:*/19";
//            String s = extractPort(subnet);
//            System.out.println(s);
//         //   boolean isValid = validateIpV4InSubnet(subnet);
//         //   System.out.println("IP in subnet: " + isValid);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {
//        System.out.println(isValidPort("*"));
//        System.out.println(isValidPort("80-90"));
//        System.out.println(isValidPort("8090"));
//        System.out.println(isValidPort("801231"));

        System.out.println(isValidPort("80-90"));
//        String ip = getIp("http://www.baidu.com:80");
//        System.out.println(ip);
//        System.out.println(isWildcardDomainName("*.www.koal.com"));
//        System.out.println(isWildcardDomainName("www.koal.com"));
//        System.out.println(isValidPortRange("80"));
//        System.out.println(isValidPortRange("*"));
//        System.out.println(isValidPortRange("80-90"));
//        System.out.println(isValidPortRange("80~90"));
//        System.out.println(isValidPortRange("123123123"));

    }

    public static boolean validateIpV4InSubnet(String subnet) throws UnknownHostException {
        String[] parts = subnet.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid subnet format");
        }

        String subnetAddress = parts[0];
        int prefixLength = Integer.parseInt(parts[1]);

        if (!isValidIp(subnet)) {
            throw new IllegalArgumentException("Invalid IP address format");
        }

        // IP的BigInteger & 子网掩码的BigInteger 如果等于ip的BigInteger  说明ip和子网掩码是匹配的
        BigInteger ip = ipToBigInteger(InetAddress.getByName(subnetAddress));
        BigInteger mask = getMask(prefixLength);
        BigInteger networkAddress = ip.and(mask);
        return networkAddress.equals(ip);
    }

    private static BigInteger ipToBigInteger(InetAddress address) {
        byte[] bytes = address.getAddress();
        return new BigInteger(1, bytes);
    }

    private static BigInteger getMask(int prefixLength) {
        // 生成一个前 prefixLength 位为 1，其余为 0 的掩码
        BigInteger mask = BigInteger.ZERO.setBit(prefixLength).subtract(BigInteger.ONE);
        // 掩码左移，将非网络部分移到高位
        mask = mask.shiftLeft(32 - prefixLength);
        // 只保留最后32位（即IPv4地址的有效部分）
        return mask.and(BigInteger.valueOf(0xFFFFFFFFL));
    }

    public static boolean isValidIPv4(String object) {
        return IPV4_PATTERN.matcher(object).matches();
    }


    public static boolean isValidCIDR(String object) {
        return CIDR_PATTERN.matcher(object).matches();
    }

    public static boolean isValidIp(String ip) {
        // 域名则不做校验
        if (isDomainName(ip)) {
            return true;
        }
        return isValidIPv4(ip) || isValidCIDR(ip);
    }

    private static boolean isDomainName(String ip) {
        // 检查是否包含字母或其他非数字字符来判断是否为域名
        return ip.matches(".*[a-zA-Z].*");
    }
    /**
     * 输入的资源对象是否为通配符域名模式
     *
     * @param domain 资源对象 eg：http://test.koal.com:12345
     * @return true/false
     */
    private static boolean isWildcardDomainName(String domain) {
        return domain.matches(".*[a-zA-Z].*") && domain.contains("*");
    }

    private static boolean isValidPort(String port) {
        return PORT_PATTERN.matcher(port).matches();
    }

    private static boolean isValidPortRange(String portRange) {
        if (!PORT_RANGE_PATTERN.matcher(portRange).matches()) {
            return false;
        }
        if (portRange.contains("-")) {
            String[] ports = portRange.split("-");
            int startPort = Integer.parseInt(ports[0]);
            int endPort = Integer.parseInt(ports[1]);
            return startPort <= endPort;
        }
        return false;
    }

    public static boolean isValidPorts(String ports) {
        return isValidPort(ports) || isValidPortRange(ports);
    }

    public static String extractPort(String object) {
        object = object.replaceAll("^(http://|https://|tcp://|cidr://)", "");
        int colonIndex = object.indexOf(':');
        String port = "";
        if (colonIndex != -1 && colonIndex < object.length() - 1) {
            String potentialPort = object.substring(colonIndex + 1);
            int slashIndex = potentialPort.indexOf('/');
            if (slashIndex != -1) {
                port = potentialPort.substring(0, slashIndex);
            } else {
                port = potentialPort;
            }
        }
        return port;
    }

    public static boolean isIpInSubnet(String cidr, String ip) {
        SubnetUtils utils = new SubnetUtils(cidr);
        return utils.getInfo().isInRange(ip);
    }

    public static String getIp(String object) {
        String ip;
        ip = object.replaceAll("^(http://|https://|tcp://|cidr://)", "");
        int colonIndex = ip.indexOf(':');
        if (colonIndex != -1) {
            // 去除端口部分
            return ip.substring(0, colonIndex);
        }
        return ip;
    }
}
