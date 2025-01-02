package com.bigshen.learningDemo.utils.encrypt;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;

public class EncryptUtil {
    private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

    /**
     * 将字节数组进行base64编码
     *
     * @param bytes
     * @return
     */
    public static String encodeBase64(byte[] bytes) {
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }

    /**
     * 将字符串进行base64解码
     *
     * @param str
     * @return
     */
    public static byte[] decodeBase64(String str) {
        byte[] bytes = null;
        bytes = Base64.getDecoder().decode(str);
        return bytes;
    }

    /**
     * 将字符串str 先使用utf-8符集进行url编码 +转为%2B ?转为%3F 等防止传输过程中出问题，然后在进行base64编码
     *
     * @param str
     * @return
     */
    public static String encodeUTF8StringBase64(String str) {
        String encoded = null;
        try {
            encoded = URLEncoder.encode(str, "utf-8");
            encoded = Base64.getEncoder().encodeToString(encoded.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            logger.warn("不支持的编码格式", e);
        }
        return encoded;

    }

    /**
     * 将字符串str 先进行base64解码 然后在使用utf-8字符集进行url解码
     *
     * @param str
     * @return
     */
    public static String decodeUTF8StringBase64(String str) {
        String decoded = null;
        byte[] bytes = Base64.getDecoder().decode(str);

        try {
            decoded = new String(bytes, "utf-8");
            decoded = URLDecoder.decode(decoded, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.warn("不支持的编码格式", e);
        }
        return decoded;
    }

    public static String base64Encoder(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());

    }

    public static byte[] base64Decoder(String str) {
        return Base64.getDecoder().decode(str);
    }


    public static void main(String[] args) {
//        String str = "www.baidu.com?a=1&b=2+1&c=dfdasaf+%&&d=1";
//        String encoded = EncryptUtil.encodeUTF8StringBase64(str);
//        String decoded = EncryptUtil.decodeUTF8StringBase64(encoded);
//        System.out.println(str);
//        System.out.println(encoded);
//        System.out.println(decoded);

        String s1 = base64Encoder("-----BEGIN CERTIFICATE-----\n" +
                "MIICFjCCAbmgAwIBAgIMdJYAAAAAAAAAAAAkMAwGCCqBHM9VAYN1BQAwJDELMAkG\n" +
                "A1UEBhMCQ04xFTATBgNVBAMMDGxvY2FsX2NhX3NtMjAeFw0yNDEyMjMwOTM4NTRa\n" +
                "Fw0yOTEyMjIwOTM4NTRaMFUxCzAJBgNVBAYTAkNOMRMwEQYDVQQIDApiZWlqaW5n\n" +
                "c2hpMRQwEgYDVQQHDAtkb25nY2hlbmdxdTEMMAoGA1UECgwDY3QyMQ0wCwYDVQQD\n" +
                "DAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEgozAxQShMNGMD9I3PgLb\n" +
                "wEnI05FbnaXG2RdugDhIdFEvrYvxwQJjqZawWW4BPvPOSR7VocodjLrLlCmladeQ\n" +
                "VqOBnTCBmjAMBgNVHRMEBTADAQEAMBYGA1UdJQEB/wQMMAoGCCsGAQUFBwMBMA4G\n" +
                "A1UdDwEB/wQEAwIAwDARBglghkgBhvhCAQEEBAMCAEAwDwYDVR0RBAgwBoIEdGVz\n" +
                "dDAfBgNVHSMEGDAWgBQBTMsySShWbI0o+TFFR4OUZ7xk2DAdBgNVHQ4EFgQUCv2G\n" +
                "7QXkMzN6siCIn9ky+dGduSowDAYIKoEcz1UBg3UFAANJADBGAiEA4ZwqGpIdghIV\n" +
                "jONghntNLz8gk34w4i1p7H2xMbNFyk4CIQCdF13ndUF+B0JsBCEEIUiJT6pszfDP\n" +
                "D/2s2anM1SlQvQ==\n" +
                "-----END CERTIFICATE-----");
        System.out.println(s1);
        String s2 = base64Encoder("-----BEGIN CERTIFICATE-----\n" +
                "MIICAzCCAaagAwIBAgIMdJYAAAAAAAAAAAAjMAwGCCqBHM9VAYN1BQAwJDELMAkG\n" +
                "A1UEBhMCQ04xFTATBgNVBAMMDGxvY2FsX2NhX3NtMjAeFw0yNDEyMjMwOTM4NTRa\n" +
                "Fw0yOTEyMjIwOTM4NTRaMFUxCzAJBgNVBAYTAkNOMRMwEQYDVQQIDApiZWlqaW5n\n" +
                "c2hpMRQwEgYDVQQHDAtkb25nY2hlbmdxdTEMMAoGA1UECgwDY3QyMQ0wCwYDVQQD\n" +
                "DAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAErebLDn4CJkuPdI8Vtl1k\n" +
                "CTgM/HhFzWH8QDfYr1ovAG6AQjhwGD1fl0MurKRQTe73QWwgiSbpY7sJ7fdAsK7e\n" +
                "nqOBijCBhzAMBgNVHRMEBTADAQEAMBYGA1UdJQEB/wQMMAoGCCsGAQUFBwMEMA4G\n" +
                "A1UdDwEB/wQEAwIAMDAfBgNVHSMEGDAWgBQBTMsySShWbI0o+TFFR4OUZ7xk2DAd\n" +
                "BgNVHQ4EFgQUxXJrTkifCLq4bdBPrH0N2eNWSrUwDwYDVR0RBAgwBoIEdGVzdDAM\n" +
                "BggqgRzPVQGDdQUAA0kAMEYCIQCecxkgDpySDqMN2AWne0kmaMvbuJ6NAgo5EKdG\n" +
                "atfa6gIhANDKTwEu0rrBkfm272gqAdfS+WSDCa79S8fGlBw2/BP6\n" +
                "-----END CERTIFICATE-----");
        System.out.println(s2);

    }


}
