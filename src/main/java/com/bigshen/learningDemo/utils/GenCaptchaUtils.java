package com.bigshen.learningDemo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * @author byj
 * @date 2024/12/12
 * @Description
 */
public class GenCaptchaUtils {

    // 使用更复杂的内部密钥
    private static final String SECRET_KEY = "superSecretKeyForInternalUseWithExtraComplexity";

    public static String generateSecureRandomString() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    /**
     * 生成一个唯一的“万能码” 用于内部接口使用
     *
     * @return 哈希值（万能码）
     */
    public static String generateUniversalCode() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-");
        byte[] hash = digest.digest(SECRET_KEY.getBytes());
        return Base64.getEncoder().encodeToString(hash);
    }

    public static void main(String[] args) {
        try {
            String universalCode = generateUniversalCode();
            System.out.println("Generated Universal Code: " + universalCode);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}