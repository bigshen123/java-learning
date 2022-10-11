package com.bigshen.learningDemo.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author byj
 * @date 2022/10/11
 * 通常是资源名称的驼峰复数，是资源在url中的路径, 也是资源类型
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Path {
    String value() default "";
}
