package com.bigshen.learningDemo.javaSE.comparator;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * @author byj
 * @date 2023/12/15
 * @Description
 */
public class BigFileMD5Calculator {
    private static final int BUFFER_SIZE = 8192;

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\NSAG\\crl\\SUBCA01.crl";
        long start = System.currentTimeMillis();
        MessageDigest md5Digest = MessageDigest.getInstance("MD5");

        try (InputStream is = Files.newInputStream(Paths.get(filePath));
             DigestInputStream dis = new DigestInputStream(is, md5Digest)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            while (dis.read(buffer) != -1) {
                // 读取文件块并更新MD5值
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
        System.out.println("MD5: " + md5);
        long end = System.currentTimeMillis();
        System.err.println("cost:" + (end - start));
    }
}
