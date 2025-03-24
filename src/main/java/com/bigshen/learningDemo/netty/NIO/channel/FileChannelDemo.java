package com.bigshen.learningDemo.netty.NIO.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author byj
 * @date 2025/3/20
 * @Description 顺序写的例子
 */
public class FileChannelDemo {
    public static void main(String[] args) throws Exception {
        //构造一个传统的文件输出流
        FileOutputStream out = new FileOutputStream("D:\\NSAG\\test.txt");
        //通过文件输出流获取到对应的FileChannel，以NIO的方式来写文件
        FileChannel channel = out.getChannel();

        ByteBuffer buffer = ByteBuffer.wrap("hello world".getBytes());
        channel.write(buffer);
        //channel必然会从buffer的position = 0的位置开始读起，一直读到limit，limit = 字符串字节数组的长度
        //buffer的position此时就已经变成了跟limit一样了

        System.out.println(buffer.position());//输出11
        System.out.println(channel.position());//输出11，当前写到了文件的哪一个位置

        //继续往磁盘文件里写，就是从FileChannel的position = 11开始写，相当于文件末尾追加
        //如果想再次将buffer里的数据通过channel写入磁盘文件末尾，就是顺序写
        buffer.rewind();//position = 0，重新读一遍
        channel.write(buffer);//在文件末尾追加写的方式，顺序写，写完后文件内容就是"hello worldhello world"

        channel.close();
        out.close();
    }



}
