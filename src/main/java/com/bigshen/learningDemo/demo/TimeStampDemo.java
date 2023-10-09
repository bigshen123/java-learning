package com.bigshen.learningDemo.demo;

/**
 * @author byj
 * @date 2023/8/31
 */
public class TimeStampDemo {

    public static void main(String[] args) {
        long l = System.currentTimeMillis();
        System.out.println(l);
        long newL = timeStampToMinuteTimeStamp(l);
        System.out.println(newL);
    }

    /**
     * 时间戳转为整分钟的
     *
     * @param timeStamp 时间戳
     * @return 整分钟的时间
     */
    static long timeStampToMinuteTimeStamp(long timeStamp) {
        return (timeStamp / 1000 - timeStamp / 1000 % 60) * 1000;
    }
}
