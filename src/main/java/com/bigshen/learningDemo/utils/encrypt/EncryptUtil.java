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
                "MIICFjCCAbmgAwIBAgIMdJYAAAAAAAAAAAAiMAwGCCqBHM9VAYN1BQAwJDELMAkG\n" +
                "A1UEBhMCQ04xFTATBgNVBAMMDGxvY2FsX2NhX3NtMjAeFw0yNDEyMjMwNjMxNTla\n" +
                "Fw0yOTEyMjIwNjMxNTlaMFUxCzAJBgNVBAYTAkNOMRMwEQYDVQQIDApiZWlqaW5n\n" +
                "c2hpMRQwEgYDVQQHDAtkb25nY2hlbmdxdTEMMAoGA1UECgwDY3QyMQ0wCwYDVQQD\n" +
                "DAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEVojN+RL6SbVBku61UUbF\n" +
                "tMq0+VtRB7OuQ39nL3sAtUy1hinE2Nl6L1zoQ4JB/D1L4fUdrlrupcll0DfIhLgK\n" +
                "laOBnTCBmjAMBgNVHRMEBTADAQEAMBYGA1UdJQEB/wQMMAoGCCsGAQUFBwMBMA4G\n" +
                "A1UdDwEB/wQEAwIAwDARBglghkgBhvhCAQEEBAMCAEAwDwYDVR0RBAgwBoIEdGVz\n" +
                "dDAfBgNVHSMEGDAWgBQBTMsySShWbI0o+TFFR4OUZ7xk2DAdBgNVHQ4EFgQUJHAv\n" +
                "Qv/H7McaGrJzkNh2Slsc+b8wDAYIKoEcz1UBg3UFAANJADBGAiEAxTeAmV84XJXq\n" +
                "Lf7jQH5zrorXq65mVVZxQDz04Yz3m5sCIQD8CdExdis8GLOb9fyG2vxIjGoeEyz6\n" +
                "n8AUyB23FJwUqA==\n" +
                "-----END CERTIFICATE-----");
        System.out.println(s1);
        String s2 = base64Encoder("-----BEGIN CERTIFICATE-----\n" +
                "MIICATCCAaagAwIBAgIMdJYAAAAAAAAAAAAhMAwGCCqBHM9VAYN1BQAwJDELMAkG\n" +
                "A1UEBhMCQ04xFTATBgNVBAMMDGxvY2FsX2NhX3NtMjAeFw0yNDEyMjMwNjMxNTla\n" +
                "Fw0yOTEyMjIwNjMxNTlaMFUxCzAJBgNVBAYTAkNOMRMwEQYDVQQIDApiZWlqaW5n\n" +
                "c2hpMRQwEgYDVQQHDAtkb25nY2hlbmdxdTEMMAoGA1UECgwDY3QyMQ0wCwYDVQQD\n" +
                "DAR0ZXN0MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEU/vc1uayuf1z6KPHFamK\n" +
                "Lu+8BkSmcJTzm76HIwQMxLRNQvqMT4aA6Wkz6xJCkZ6sunFxnrP71hr2xhZ80JP2\n" +
                "p6OBijCBhzAMBgNVHRMEBTADAQEAMBYGA1UdJQEB/wQMMAoGCCsGAQUFBwMEMA4G\n" +
                "A1UdDwEB/wQEAwIAMDAfBgNVHSMEGDAWgBQBTMsySShWbI0o+TFFR4OUZ7xk2DAd\n" +
                "BgNVHQ4EFgQUcm5UDwilCnB8fXYbtbQGZU2sOfgwDwYDVR0RBAgwBoIEdGVzdDAM\n" +
                "BggqgRzPVQGDdQUAA0cAMEQCIHkmHzmmDU5LLtZdzsS1FWcSrB4O4EqZDDOCdAyw\n" +
                "7OJTAiALy1ovhGkUjBIPkSZVVsfAhcebuiD5sAMSi/eYmusrrA==\n" +
                "-----END CERTIFICATE-----");
        System.out.println(s2);

    }


}
