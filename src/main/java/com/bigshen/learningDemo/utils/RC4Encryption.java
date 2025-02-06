package com.bigshen.learningDemo.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author byj
 * @date 2025/1/14
 * @Description
 */
public class RC4Encryption {
    // 固定密钥
    private static final String SECRET_KEY = "K0a120070501CHYZ";

    // RC4 加密函数
    public static byte[] rc4Encrypt(byte[] data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "RC4");
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    // Base64 编码
    public static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    // 综合加密并编码
    public static String encryptAndEncode(String data) throws Exception {
        // 1. RC4 加密
        byte[] encryptedData = rc4Encrypt(data.getBytes());

        // 2. Base64 编码
        return base64Encode(encryptedData);
    }

    public static void main(String[] args) {
        try {
            String plaintext = "Hello, this is a test message!";
            String encryptedAndEncoded = encryptAndEncode(plaintext);
            System.out.println("Encrypted and Base64 encoded result: " + encryptedAndEncoded);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
