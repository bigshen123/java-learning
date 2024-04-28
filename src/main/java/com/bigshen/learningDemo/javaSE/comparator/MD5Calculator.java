package com.bigshen.learningDemo.javaSE.comparator;

import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * @author byj
 * @date 2023/12/15
 * @Description
 */
public class MD5Calculator {
    public static void main(String[] args) throws Exception {
        String filePath = "D:\\NSAG\\crl\\SUBCA01.crl";
        long start = System.currentTimeMillis();
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        try (FileInputStream fis = new FileInputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                md5Digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] md5Bytes = md5Digest.digest();

        StringBuilder md5Builder = new StringBuilder();
        for (byte md5Byte : md5Bytes) {
            String hex = Integer.toHexString(0xff & md5Byte);
            if (hex.length() == 1) {
                md5Builder.append('0');
            }
            md5Builder.append(hex);
        }

        String md5 = md5Builder.toString();
        long end = System.currentTimeMillis();
        System.err.println("cost:" + (end - start));
        System.out.println("MD5: " + md5);
    }
}
