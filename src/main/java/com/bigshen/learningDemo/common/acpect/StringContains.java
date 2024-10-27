package com.bigshen.learningDemo.common.acpect;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 校验一个字符串是否包含在指定字符数组内
 */
@Documented
@Constraint(validatedBy = StringContainsValidator.class)
@Target({ ElementType.PARAMETER,ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface StringContains {

    String message() default "字符串不符合规则";

    String[] limitValues() default {};

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
