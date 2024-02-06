package com.bigshen.learningDemo.demo.comparator;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author byj
 * @date 2023/12/14
 * @Description
 */
public class LargeFileComparator {

    public static boolean compareLargeFiles(String filePath1, String filePath2) throws IOException, NoSuchAlgorithmException {
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);

        if (!file1.exists() || !file2.exists() || file1.length() != file2.length()) {
            return false;
        }

        try (FileChannel channel1 = FileChannel.open(Paths.get(filePath1), StandardOpenOption.READ);
             FileChannel channel2 = FileChannel.open(Paths.get(filePath2), StandardOpenOption.READ)) {

            MessageDigest digest1 = MessageDigest.getInstance("SHA-256");
            MessageDigest digest2 = MessageDigest.getInstance("SHA-256");

            long position = 0;
            long size = channel1.size();
            int bufferSize = 8192;

            while (position < size) {
                long remaining = size - position;
                long bytesRead = Math.min(remaining, bufferSize);

                ByteBuffer buffer1 = ByteBuffer.allocate((int) bytesRead);
                ByteBuffer buffer2 = ByteBuffer.allocate((int) bytesRead);

                channel1.read(buffer1, position);
                channel2.read(buffer2, position);

                digest1.update(buffer1);
                digest2.update(buffer2);

                if (!MessageDigest.isEqual(digest1.digest(), digest2.digest())) {
                    return false;
                }

                position += bytesRead;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        String filePath1 = "D:\\NSAG\\crl\\SUBCA01.crl";
        String filePath2 = "D:\\NSAG\\crl\\SUBCA02.crl";

        try {
            long start = System.currentTimeMillis();
            if (compareLargeFiles(filePath1, filePath2)) {
                System.out.println("文件内容一致");
            } else {
                System.out.println("文件内容不一致");
            }
            long end = System.currentTimeMillis();
            System.err.println("cost:" + (end - start));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
