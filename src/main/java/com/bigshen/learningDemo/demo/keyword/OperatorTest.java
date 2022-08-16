package com.bigshen.learningDemo.demo.keyword;

import org.junit.Test;

/**
 * @author byj
 * @date 2022/8/15
 */
public class OperatorTest {

    @Test
    public void test01() {
        // 6转成二进制
        // 0000 0000 0000 0000 0000 0000 0000 0000 0110
        System.out.println(Integer.toBinaryString(6));
        // 有符号左移1位
        // 这个意思就是将所有的二进制位向左边移动 1 位，那就变成了
        // 0000 0000 0000 0000 0000 0000 0000 0000 1100
        // 又比如我现在计算6 << 10，那就变成了左移动 10 位，得出结果
        // 0000 0000 0000 0000 0000 0001 1000 0000 0000
        System.out.println(Integer.toBinaryString(6 << 1));
        System.out.println(6 << 1);


        // -6转成二进制
        // 这里说明一下负数如何转为二进制，其实就是求反码再加1
        // 1111 1111 1111 1111 1111 1111 1111 1111 1010
        System.out.println(Integer.toBinaryString(-6));
        // 有符号左移1位
        // 1111 1111 1111 1111 1111 1111 1111 1111 0100
        System.out.println(Integer.toBinaryString(-6 << 1));
        System.out.println(-6 << 1);
    }


    @Test
    public void test02() {
        // 6转成二进制
        // 0000 0000 0000 0000 0000 0000 0000 0000 0110
        System.out.println(Integer.toBinaryString(6));
        // 有符号右移1位
        // 这个意思就是将所有的二进制位向右边移动 1 位，那就变成了
        // 0000 0000 0000 0000 0000 0000 0000 0000 0011
        // 又比如我现在计算6 << 10，那就变成了右移动 10 位，得出结果
        // 0000 0000 0000 0000 0000 0000 0000 0000 0000
        System.out.println(Integer.toBinaryString(6 >> 1));
        System.out.println(6 >> 1);

        // -6转成二进制
        // 这里说明一下负数如何转为二进制，其实就是求反码再加1
        // 1111 1111 1111 1111 1111 1111 1111 1111 1010
        System.out.println(Integer.toBinaryString(-6));

        // 有符号右移1位
        // 1111 1111 1111 1111 1111 1111 1111 1111 1101
        System.out.println(Integer.toBinaryString(-6 >> 1));
        System.out.println(-6 >> 1);
    }

    @Test
    public void test03() {
        // 6转成二进制
        // 0000 0000 0000 0000 0000 0000 0000 0000 0110
        System.out.println(Integer.toBinaryString(6));

        // 无符号右移1位
        // 0000 0000 0000 0000 0000 0000 0000 0000 0011
        // 又比如我现在计算6 >>> 10，那就变成了右移动 10 位，得出结果
        // 0000 0000 0000 0000 0000 0000 0000 0000 0000
        System.out.println(Integer.toBinaryString(6 >>> 1));
        System.out.println(6 >>> 1);
    }

    @Test
    public void test04() {
        // -6转成二进制
        // 1111 1111 1111 1111 1111 1111 1111 1111 1010
        System.out.println(Integer.toBinaryString(-6));

        // 无符号右移1位
        // 0111 1111 1111 1111 1111 1111 1111 1111 1101
        System.out.println(Integer.toBinaryString(-6 >>> 1));
        System.out.println(-6 >>> 1);
    }


}
