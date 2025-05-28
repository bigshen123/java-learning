package com.bigshen.springbootDemo.annotation;

import java.lang.annotation.*;

/**
 * @author byj
 * @date 2025/5/28
 * @Description
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PassToken {
    boolean canPass() default  false;
}
