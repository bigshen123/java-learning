package com.bigshen.learningDemo.jvm;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author byj
 * @date 2024/7/10
 * @Description 直接内存
 */
public class DirectMemory {


    public static int size = 1024 * 1024 * 100;
    public static List<ByteBuffer> list = new ArrayList<ByteBuffer>();
    public static int count = 0;

    /**
     * 设置虚拟机参数 -XX:MaxDirectMemorySize=1g
     * @param args
     * @throws IOException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        System.in.read();
        while (true) {
            ByteBuffer directBuffer = ByteBuffer.allocateDirect(size);
            list.add(directBuffer);
            System.out.println(++count);
        }
    }
}
