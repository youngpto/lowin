package com.young.lowin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created with IntelliJ IDEA.
 * Description: 限制角色访问
 * Author: young
 * Date: 2021-08-26
 * Time: 19:21
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiredRoles {

    String[] value();

    Logical logical() default Logical.OR;
}
