package com.bigshen.learningDemo.leetcode.sort.largeFile;

/**
 * @Author BYJ
 * @Date 2025/2/9 21:38
 * @Describe  内存200M读取1G文件并统计内容重复次数？

 * 读取大文件并存储到多个小文件
 * 步骤 1：分块存储
 * 逐行读取 1GB 文件内容。
 * 计算哈希值（如 SHA-256）以减少存储开销（可选）。
 * 将每个内容写入本地存储文件（如 tmp_data_partX.txt）。
 * 使用 哈希取模 将内容分散到多个小文件中，减少单个文件大小。
 */
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class LargeFileSplitter {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String inputFile = "large_input.txt"; // 1GB 原始大文件
        String outputDir = "split_files"; // 存储小文件的目录

        long fileSize = new File(inputFile).length(); // 获取文件大小

        /**
         * 动态分片大小
         * 分片数 = (文件大小 / 目标单个文件大小)
         * 大文件是 1GB = 1024MB  希望 每个小文件控制在 20MB
         */
        int numParts = (int) Math.max(10, fileSize / (20 * 1024 * 1024)); // 计算分片数，至少10个
        System.out.println("文件大小: " + fileSize + " 字节，分片数: " + numParts);

        // 创建存储目录
        new File(outputDir).mkdirs();

        // 生成多个文件流
        BufferedWriter[] writers = new BufferedWriter[numParts];
        for (int i = 0; i < numParts; i++) {
            writers[i] = new BufferedWriter(new FileWriter(outputDir + "/part_" + i + ".txt"));
        }

        // 读取大文件
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        MessageDigest md = MessageDigest.getInstance("SHA-256"); // 计算哈希值

        String line;
        while ((line = reader.readLine()) != null) {
            // 计算哈希值并取模，分配到不同文件
            byte[] hashBytes = md.digest(line.getBytes());
            int hash = Math.abs(Arrays.hashCode(hashBytes)) % numParts;
            writers[hash].write(line);
            writers[hash].newLine();
        }

        // 关闭所有文件流
        reader.close();
        for (BufferedWriter writer : writers) {
            writer.close();
        }

        System.out.println("文件分块存储完成！");
    }
}

