package com.bigshen.learningDemo.leetcode.sort.largeFile;

/**
 * @Author BYJ
 * @Date 2025/2/9 21:38
 * @Describe 读取小文件并统计重复次数
 * <p>
 * 步骤 2：统计文件
 * 读取存储文件中的内容。
 * 使用 外部排序 方式统计重复次数（适用于超大文件）。
 * 最终输出去重统计后的数据。
 */

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LargeFileCounter {
    private static final String OUTPUT_FILE = "final_output.txt"; // 统计后的结果存储

    public static void main(String[] args) throws IOException {
        String inputFile = "large_input.txt"; // 1GB 原始大文件
        String inputDir = "split_files"; // 读取切分后的文件
        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

        long fileSize = new File(inputFile).length(); // 获取文件大小
        int numParts = (int) Math.max(10, fileSize / (20 * 1024 * 1024)); // 计算分片数，至少10个
        System.out.println("文件大小: " + fileSize + " 字节，分片数: " + numParts);
        for (int i = 0; i < numParts; i++) {
            String partFile = inputDir + "/part_" + i + ".txt";
            System.out.println("处理文件: " + partFile);

            // 统计当前文件中的重复内容
            Map<String, Integer> countMap = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader(partFile));
            String line;
            while ((line = reader.readLine()) != null) {
                countMap.put(line, countMap.getOrDefault(line, 0) + 1);
            }
            reader.close();

            // 将结果写入最终统计文件
            for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue()); // CSV 格式
                writer.newLine();
            }
        }

        writer.close();
        System.out.println("统计完成！结果存储在：" + OUTPUT_FILE);
    }
}

