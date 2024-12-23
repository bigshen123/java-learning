package com.bigshen.learningDemo.javaSE.comparator;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author byj
 * @date 2023/12/14
 * @Description
 */
public class ReadByteComparator {
    // 32KB 缓冲区大小
    private static final int BUFFER_SIZE = 1024 * 32;

    public static void main(String[] args) {
        File filePath1 = new File("C:\\Users\\Lenovo\\Desktop\\backup\\test\\d382a70d-2449-480f-9f54-0c38d9d3bc84.crl");
        File filePath2 = new File("D:\\NSAG\\crl\\testSubpart0group02.crl");


        // boolean b = equalNewCrlContentWithOldCrlContent(filePath1, filePath2);
        // System.out.println(b);
        // boolean b1 = compareCrlPrivateKey(filePath1, filePath2);
        // System.out.println(b1);

//        boolean b = equalNewCrlContentWithOldCrlContent2(filePath1, filePath2);
//        System.out.println(b);
        calculateCrlPrivateKeyMd5Hex(filePath2);
//        List<File> oldFiles = new ArrayList<>();
//        oldFiles.add(filePath1);
//        boolean b;
//        b = equalNewCrlContentWithOldCrlContentByTenantId(oldFiles, filePath2);
//        System.out.println(b);
    }


    public static String calculateCrlPrivateKeyMd5Hex(File newFile) {
        int byteLength = 1024 * 32;
        ByteBuffer newBuffer = ByteBuffer.allocate(byteLength);
        try (FileChannel newChannel = FileChannel.open(newFile.toPath(), StandardOpenOption.READ)) {
            long newFileSize = newChannel.size();
            long length = Math.min(newFileSize, byteLength);
            newChannel.position(newFileSize - length);
            newChannel.read(newBuffer);
            newBuffer.flip();
            byte[] array = new byte[newBuffer.remaining()];
            newBuffer.get(array);
            String md5 = DigestUtils.md5Hex(array);
            System.out.println("md5:" + md5);
            return md5;
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean equalNewCrlContentWithOldCrlContentByTenantId(List<File> oldFiles, File newFile) {
        if (!newFile.exists()) {
            return false;
        }
        int byteLength = 1024 * 32;

        ByteBuffer oldBuffer = ByteBuffer.allocate(byteLength);
        ByteBuffer newBuffer = ByteBuffer.allocate(byteLength);

        try (FileChannel newChannel = FileChannel.open(newFile.toPath(), StandardOpenOption.READ)) {
            long newFileSize = newChannel.size();
            for (File crl : oldFiles) {
                try (FileChannel oldChannel = FileChannel.open(Paths.get(crl.toURI()), StandardOpenOption.READ)) {
                    long oldFileSize = oldChannel.size();
                    if (oldFileSize != newFileSize) {
                        continue;
                    }
                    long length = oldFileSize < byteLength ? oldFileSize : byteLength;
                    newChannel.read(newBuffer, newFileSize - length);
                    oldChannel.read(oldBuffer, oldFileSize - length);
                    boolean res = Arrays.equals(oldBuffer.array(), newBuffer.array());
                    oldBuffer.clear();
                    newBuffer.clear();
                    if (res) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static boolean compareCrlPrivateKey(File oldCrlFile, File newCrlFile) {
        if (!newCrlFile.exists() || !oldCrlFile.exists()) {
            return false;
        }

        try (FileChannel oldChannel = new FileInputStream(oldCrlFile).getChannel();
             FileChannel newChannel = new FileInputStream(newCrlFile).getChannel()) {

            long oldFileSize = oldChannel.size();
            long newFileSize = newChannel.size();
            if (oldFileSize != newFileSize) {
                return false;
            }

            // 移动文件指针至最后 32KB 处
            oldChannel.position(Math.max(oldFileSize - BUFFER_SIZE, 0));
            newChannel.position(Math.max(newFileSize - BUFFER_SIZE, 0));

            ByteBuffer oldBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            ByteBuffer newBuffer = ByteBuffer.allocate(BUFFER_SIZE);

            oldChannel.read(oldBuffer);
            newChannel.read(newBuffer);

            oldBuffer.flip();
            newBuffer.flip();

            return oldBuffer.equals(newBuffer);

        } catch (IOException e) {
            return false;
        }
    }


    public static boolean equalNewCrlContentWithOldCrlContent2(File oldFile, File newFile) {
        if (!newFile.exists()) {
            return false;
        }

        try (FileChannel newChannel = FileChannel.open(newFile.toPath(), StandardOpenOption.READ);
             FileChannel oldChannel = FileChannel.open(oldFile.toPath(), StandardOpenOption.READ)) {

            long oldFileSize = oldChannel.size();
            long newFileSize = newChannel.size();
            if (oldFileSize != newFileSize) {
                return false;
            }

            int byteLength = 1024 * 32;
            long position = Math.max(oldFileSize - byteLength, 0);
            long size = Math.min(byteLength, oldFileSize);
            MappedByteBuffer oldBuffer = oldChannel.map(FileChannel.MapMode.READ_ONLY, position, size);
            MappedByteBuffer newBuffer = newChannel.map(FileChannel.MapMode.READ_ONLY, position, size);

            // 逐字节比较两个缓冲区的内容
            while (oldBuffer.hasRemaining() && newBuffer.hasRemaining()) {
                if (oldBuffer.get() != newBuffer.get()) {
                    return false; // 发现不同的字节，返回false
                }
            }

            return true; // 缓冲区内容完全相同，返回true
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static boolean equalNewCrlContentWithOldCrlContent(List<File> oldFiles, File newFile) throws IOException {
        if (!newFile.exists() || oldFiles.isEmpty()) {
            return false;
        }

        try (FileChannel newChannel = FileChannel.open(newFile.toPath(), StandardOpenOption.READ)) {
            long newFileSize = newChannel.size();
            int byteLength = 1024 * 32;
            long position = Math.max(newFileSize - byteLength, 0);
            long size = Math.min(byteLength, newFileSize);
            MappedByteBuffer newBuffer = newChannel.map(FileChannel.MapMode.READ_ONLY, position, size);
            for (File oldFile : oldFiles) {
                if (!oldFile.exists()) {
                    continue;
                }
                try (FileChannel oldChannel = FileChannel.open(oldFile.toPath(), StandardOpenOption.READ)) {
                    long oldFileSize = oldChannel.size();
                    if (oldFileSize != newFileSize) {
                        continue;
                    }
                    MappedByteBuffer oldBuffer = oldChannel.map(FileChannel.MapMode.READ_ONLY, position, size);
                    if (oldBuffer.equals(newBuffer)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
