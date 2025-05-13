package com.bigshen.springbootDemo.annotation.log;

import java.lang.annotation.*;

/**
 * @author byj
 * @date 2025/4/16
 * @Description
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogExecution {
    String value() default "";
}