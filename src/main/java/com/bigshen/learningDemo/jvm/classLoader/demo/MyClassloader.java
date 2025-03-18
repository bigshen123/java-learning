package com.bigshen.learningDemo.jvm.classLoader.demo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 自定义的类加载器
 * @author byj
 * @date 2025/2/19
 * @Description
 */
public class MyClassloader extends ClassLoader {

    private String classRootDir;

    public MyClassloader(String classRootDir) {
        this.classRootDir = classRootDir;
    }

    @Override
    protected Class<?> findClass(String className) throws ClassNotFoundException {
        // 读取Class的二进制数据
        byte[] classBytes = getClassBytes (className);
        return super.defineClass(className, classBytes, 0, classBytes.length);
    }

    private byte[] getClassBytes(String className) {
        // 解析出class文件在磁盘上的绝对路径
        String classFilePath = resolveClassFilePath(className);

        // 将Class文件读取为二进制数组
        ByteArrayOutputStream bytesReader;
        try (InputStream is = new FileInputStream(classFilePath)) {
            bytesReader = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int readSize = 0;
            while ((readSize = is.read(buffer)) != -1) {
                bytesReader.write(buffer, 0, readSize);
            }
            return bytesReader.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private String resolveClassFilePath(String className) {
        return this.classRootDir + File.separatorChar + className.replace('.', File.separatorChar) + ".class";
    }

}