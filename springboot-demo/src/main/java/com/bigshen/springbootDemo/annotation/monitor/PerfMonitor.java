package com.bigshen.springbootDemo.annotation.monitor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义性能监控注解
 * 用于标记需要监控执行时间的方法
 * @author byj
 * @date 2025/4/16
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PerfMonitor {

    // 执行时间阈值，单位毫秒，默认为 1000ms
    long threshold() default 1000;

    // 是否开启监控，默认是开启
    boolean enable() default true;

    // 执行时的日志级别：INFO/WARNING/ERROR
    String level() default "INFO";

}
