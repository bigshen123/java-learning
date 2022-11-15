package kl.gw.cloud.rms.util;

import inet.ipaddr.AddressStringException;
import inet.ipaddr.IPAddressString;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 网络相关工具类
 *
 * @author xuhui on 2021/5/18.
 */
public class NetUtil {

    public final static String URL_SEPARATOR = "/";

    /**
     * IPv4 32位子网掩码
     */
    public final static String HOST_NETMASK = "255.255.255.255";

    /**
     * IPv6 128位子网掩码
     */
    public final static String HOST_V6_NETMASK = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff";

    /**
     * 检查网络端口是否打开
     *
     * @param ip   IP地址
     * @param port 端口
     * @return 网络端口是否打开
     */
    public static boolean checkNet(String ip, int port) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(ip, port), 2000);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 检测IP是否可以连通
     *
     * @param ipAddress ip地址
     * @param timeout   超时时间,单位毫秒
     * @return 是否可以连通
     */
    public static Boolean ipDetection(String ipAddress, Integer timeout) {
        // 当返回值是true时，说明host是可用的，false则不可。
        boolean status = false;
        try {
            status = InetAddress.getByName(ipAddress).isReachable(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }


    /**
     * 组装url
     *
     * @param service  服务地址
     * @param subPaths 路径
     * @return 完整地址
     */
    public static String urlMerge(String service, String... subPaths) {
        service = service.endsWith(URL_SEPARATOR) ? service : (service + URL_SEPARATOR);
        if (ArrayUtils.isNotEmpty(subPaths)) {
            StringBuilder serviceBuilder = new StringBuilder(service);
            for (String path : subPaths) {
                path = path.startsWith(URL_SEPARATOR) ? path.substring(1) : path;
                serviceBuilder.append(path).append(URL_SEPARATOR);
            }
            if (serviceBuilder.toString().endsWith(URL_SEPARATOR)) {
                service = serviceBuilder.deleteCharAt(serviceBuilder.length() - 1).toString();
            }
        }
        return service;
    }

    /**
     * 子网掩码转子网掩码位数
     *
     * @param netmask 子网掩码
     * @return 子网掩码位数
     * @throws AddressStringException 异常
     */
    public static int netmaskToPrefix(String netmask) throws AddressStringException {
        IPAddressString addressString = new IPAddressString(HOST_NETMASK + "/" + netmask);
        return addressString.toAddress().getNetworkPrefixLength();
    }

    /**
     * 子网掩码转子网掩码位数 IPV6
     *
     * @param netmask 子网掩码
     * @return 子网掩码位数
     * @throws AddressStringException 异常
     */
    public static int netmaskToV6Prefix(String netmask) throws AddressStringException {
        IPAddressString addressString = new IPAddressString(HOST_V6_NETMASK + "/" + netmask);
        return addressString.toAddress().getNetworkPrefixLength();
    }

    /**
     * 子网掩码位数转子网掩码
     *
     * @param prefix 子网掩码位数
     * @return 子网掩码
     */
    public static String prefixToNetMask(int prefix) throws AddressStringException {
        IPAddressString addressString = new IPAddressString(HOST_NETMASK + "/" + prefix);
        return addressString.toAddress().getNetwork().getNetworkMask(prefix, false).toString();
    }

    /**
     * 子网掩码位数转子网掩码 IPV6
     *
     * @param prefix 子网掩码位数
     * @return 子网掩码
     */
    public static String prefixToV6NetMask(int prefix) throws AddressStringException {
        IPAddressString addressString = new IPAddressString(HOST_V6_NETMASK + "/" + prefix);
        return addressString.toAddress().getNetwork().getNetworkMask(prefix, false).toString();
    }


}
