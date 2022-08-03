/*
 * @Copyright: 2018-2019 taojiji All rights reserved.
 */
package com.bigshen.learningDemo.utils.copy;

import java.lang.annotation.*;


@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequireField {
}
