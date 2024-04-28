package com.bigshen.learningDemo.JUC.threadLocal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @Author BYJ
 * @Date 2024/4/23 20:19
 * @Describe
 */
public class DateUtils {
    public static final ThreadLocal<DateFormat> DF = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd"));
}
