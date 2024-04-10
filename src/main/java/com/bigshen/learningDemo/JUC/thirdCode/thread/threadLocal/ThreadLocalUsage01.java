package com.bigshen.learningDemo.JUC.thirdCode.thread.threadLocal;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName ThreadLocalUsage01
 * @Description: TODO
 * @Author BYJ
 * @Date 2020/7/20
 * @Version V1.0
 **/
public class ThreadLocalUsage01 {
    public static void main(String[] args) {
        new Thread(() -> {
            String date = new ThreadLocalUsage01().date(10);
            System.out.println(date);
        }).start();

        new Thread(() -> {
            String date = new ThreadLocalUsage01().date(1000);
            System.out.println(date);
        }).start();
    }

    private String date(int seconds) {
        // 参数的单位是毫秒，从1970.1.1 00:00:00 GMT计时
        Date date = new Date(1000 * seconds);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }
}
