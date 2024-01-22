package com.bigshen.learningDemo.demo.jna;

/**
 * @Author BYJ
 * @Date 2024/1/22 22:01
 * @Describe
 * 通过 gcc -shared -o add.dll add.c 命令生成 add.dll 库文件
 * 将add.dll文件放置在运行Java程序的当前工作目录
 */
public class JnaDemo {

    public static void main(String[] args) {
        // 调用DLL中的函数
        int result = AddLibrary.INSTANCE.add(5, 7);

        // 打印结果
        System.out.println("Result: " + result);
    }
}
