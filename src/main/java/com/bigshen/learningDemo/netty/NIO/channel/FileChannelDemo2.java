package com.bigshen.learningDemo.netty.NIO.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author byj
 * @date 2025/3/20
 * @Description 随机写的例子
 */
public class FileChannelDemo2 {
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
        //如果继续往磁盘文件里写，就是从FileChannel的position = 11开始写，相当于文件末尾追加
        //现在已经在文件写入hello world，要继续在hello和world中间的那个空格地方，再写入一条数据，比如hello world
        //把这一段数据插入到磁盘文件的中间，就是磁盘随机写

        //在文件的随机的位置写入数据，肯定是要再次从buffer中读取数据，所以position必须复位
        buffer.rewind();

        //其次如果你要基于FileChannel随机写，可以调整FileChannel的position，这样文件内容为"hellohello world"
        channel.position(5);
        channel.write(buffer);

        channel.close();
        out.close();
    }
}
