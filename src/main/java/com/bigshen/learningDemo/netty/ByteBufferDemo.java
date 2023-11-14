package com.bigshen.learningDemo.netty;

import com.bigshen.learningDemo.utils.json.JacksonUtil;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

/**
 * @author byj
 * @date 2023/10/31
 * Bytebuffer 读写底层原理 数据读写主要采用三个参数来控制
 * 1.position：起始下标
 * 2.limit：限制下标
 * 3.capacity：buffer的容量
 *
 * 核心思想：Bytebuffer的读写共用position、limit参数，因此需要切换至读模式(调用flip())和写模式(调用)
 */
public class ByteBufferDemo {
    public static void main(String[] args) {
//        String msg = "hello word";
//        ByteBuffer buf  = ByteBuffer.wrap(msg.getBytes());
//        System.out.println(buf);
//        ByteBuffer buf2 = ByteBuffer.allocate(1024);
//        System.out.println(buf2);
//        buf.put(msg.getBytes());
//        System.out.println(buf);
        Set<String> tests = new HashSet<>();
        tests.add("11");
        tests.add("22");
        tests.remove("11");
        tests.remove("12312");
        System.out.println(JacksonUtil.toJsonString(tests));
    }
}
