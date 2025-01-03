package com.bigshen.learningDemo.javaSE.string;

import java.util.Random;

/**
 * @author byj
 * @date 2025/1/3
 * @Description String.intern 方法
 *
 * 在 Jdk6 以及以前的版本中，字符串的常量池是放在堆的 Perm 区的，Perm 区是一个类静态的区域,所以字符串常量池的内存是固定的，如果不断的往字符串常量池中加入字符串，迟早会导致 Perm 区的内存溢出。
 * 所以在 jdk7 的版本中，字符串常量池已经从 Perm 区移到正常的 Java Heap 区域了，这样就可以避免 Perm 区的内存溢出。
 * jdk8 取消了 Perm 永久代
 */
public class InternDemo {

    static final int MAX = 1000 * 10000;
    static final String[] arr = new String[MAX];

    public static void main(String[] args) {
        test();

        Integer[] DB_DATA = new Integer[10];
        Random random = new Random(10 * 10000);
        for (int i = 0; i < DB_DATA.length; i++) {
            DB_DATA[i] = random.nextInt();
        }
        long t = System.currentTimeMillis();
        for (int i = 0; i < MAX; i++) {
            //arr[i] = new String(String.valueOf(DB_DATA[i % DB_DATA.length]));
            arr[i] = new String(String.valueOf(DB_DATA[i % DB_DATA.length])).intern();
        }

        System.out.println((System.currentTimeMillis() - t) + "ms");
        System.gc();
    }

    private static void test() {
        String s1 = new String("1");
        s1.intern();
        String s2 = "1";
        System.out.println(s1 == s2); //false

        String s3 = new String("1") + new String("1");
        s3.intern();
        String s4 = "11";
        System.out.println(s3 == s4); // jdk6:false   jdk7:true
    }
    
}
