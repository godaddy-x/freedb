package com.pithy.free.anno;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    // 表名
    String name() default "";

    // 主键名
    String pk() default "";

}