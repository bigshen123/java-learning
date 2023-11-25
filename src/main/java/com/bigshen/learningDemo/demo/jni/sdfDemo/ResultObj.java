package com.bigshen.learningDemo.jni.sdfDemo;

/**
 * @Author BYJ
 * @Date 2023/11/16 22:13
 * @Describe
 */
public class ResultObj {
    // 接口返回值
    public int ret_code;
    // 接口错误提示信息
    public String err_msg;
    // 接口返回的byte[]类型数据，通常表示句柄指针
    public byte[] handle;
    // 接口返回的byte[]类型数据,通常表示密钥,密文
    public byte[] data;
    // 接口返回的额外的int类型数据
    public int extra_int;
    // 接口返回的额外的string类型数据
    public String extra_string;
}
