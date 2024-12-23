package com.bigshen.learningDemo.javaSE.comparator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * @author byj
 * @date 2024/12/23
 * @Description
 */
public class FileBase64Comparison {

    public static void main(String[] args) {
        try {
            // 文件路径
            File filePath2 = new File("D:\\NSAG\\crl\\KoalCa.crl");

            // 读取文件内容为字节数组
            byte[] fileBytes = Files.readAllBytes(filePath2.toPath());

            // Base64 编码文件内容
            String base64Encoded = Base64.getEncoder().encodeToString(fileBytes);

            // 打印 Base64 编码后的内容
            System.out.println("Base64 Encoded Content:");
            System.out.println(base64Encoded);

            // 示例：与指定的 Base64 字符串进行比较
            String givenBase64String = "YOUR_BASE64_STRING_HERE"; // 替换为实际的 Base64 字符串
            if (base64Encoded.equals(givenBase64String)) {
                System.out.println("The Base64 encoded content matches the given string.");
            } else {
                System.out.println("The Base64 encoded content does not match the given string.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading file or encoding content.");
        }
    }
}
