package com.bigshen.learningDemo.javaSE.io.NIO.FileChannel;


import org.junit.Test;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;


/**
 * @Author BYJ
 * @Date 2022/5/17 9:29
 * @Describe
 */
public class FileChannelDemo {


    /**
     *使用FileChannel读取数据到Buffer（缓冲区）以及利用Buffer（缓冲区）写入数据到FileChannel
     *
     * 开启FileChannel
     * 无法直接打开FileChannel（FileChannel是抽象类）。需要通过InputStream、OutputStream或RandomAccessFile获取FileChannel
     * 从FileChannel读取/写入数据
     * 关闭FileChannel
     */
    @Test
    public void FileChannelTest() throws IOException {
        //1.创建一个RandomAccessFile（随机访问文件）对象
        RandomAccessFile randomAccessFile =new RandomAccessFile("D:\\IdeaProjects\\learnning-demo\\src\\main\\resources\\test.txt","rw");
        FileChannel fileChannel = randomAccessFile.getChannel();
        //2.创建一个读取缓冲区的对象
        ByteBuffer byteBufferRead = ByteBuffer.allocate(48);
        //3.从通道中读取数据
        int bytesRead = fileChannel.read(byteBufferRead);
        //4.创建一个写数据缓冲区对象
        ByteBuffer byteBufferWrite = ByteBuffer.allocate(48);
        //5.写入数据
        byteBufferWrite.put("FileChannel test".getBytes());
        byteBufferWrite.flip();
        fileChannel.write(byteBufferWrite);
        while (bytesRead != -1){
            System.out.println("Read "+bytesRead);
            //Buffer有两种模式，写模式和读模式。在写模式下调用flip()之后，Buffer从写模式变成读模式
            byteBufferRead.flip();
            // 读取buffer中的数据
            while (byteBufferRead.hasRemaining()) {
                System.out.print((char) byteBufferRead.get());
            }
            //清空缓冲区（切换到写模式）
            byteBufferRead.clear();
            bytesRead = fileChannel.read(byteBufferRead);
        }
        // 关闭RandomAccessFile对象
        randomAccessFile.close();
    }

    /**
     * NIO通道实现文件读写
     * @throws IOException
     */
    @Test
    public void FileReadWrite() throws IOException {
        // 获得一个根据指定文件路径的读写权限文件通道
        FileChannel fileChannel = FileChannel.open(new File("D:\\IdeaProjects\\learnning-demo\\src\\main\\resources\\test.txt").toPath(),
                StandardOpenOption.WRITE, StandardOpenOption.READ);
        // 获得一段有指定内容的缓冲区
        ByteBuffer source = ByteBuffer.wrap("Hello world".getBytes(StandardCharsets.UTF_8));
        // 空的缓冲区
        ByteBuffer target = ByteBuffer.allocate(50);
        // 将缓冲区中的内容写入文件通道
        fileChannel.write(source);
        System.out.println("通道大小：" + fileChannel.position());
        //设置读写的位置
        fileChannel.position(0);
        // 将通道中的内容写到空缓冲区
        fileChannel.read(target);
        // 将缓冲区字节数据转为字符串
        System.out.println(new String(target.array()));
        // 转换缓冲区读写模式
        target.flip();
        //关闭资源
        fileChannel.close();
    }

    /**
     * 需求：将一个视频文件从F:\\Channel\\a.mp4复制到F:\\Channel\\b.mp4
     */
    @Test
    public void FileCopyNioTest() throws IOException {
        // 准备输入流(源文件)
        FileInputStream fileInputStream = new FileInputStream("F:\\Channel\\a.mp4");
        // 准备输出流（目标文件）
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\Channel\\b.mp4");

        // 根据流获取通道
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();

        // 指向复制方法
        // outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());
        inputStreamChannel.transferTo(0, inputStreamChannel.size(), outputStreamChannel);
        // 关闭资源
        fileInputStream.close();
        fileOutputStream.close();
    }
    /**
     * 原生 BIO 复制文件操作
     */
    @Test
    public void FileCopyBioTest() throws IOException {
        // 准备输入流(源文件)
        FileInputStream fileInputStream = new FileInputStream("F:\\Channel\\a.mp4");
        // 准备输出流（目标文件）
        FileOutputStream fileOutputStream = new FileOutputStream("F:\\Channel\\b.mp4");
        //存储数据的字节数组
        byte[] bytes = new byte[1024];
        while (true){
            int res = fileInputStream.read(bytes);
            if (res == -1){
                break;
            }
            fileOutputStream.write(bytes,0,res);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
