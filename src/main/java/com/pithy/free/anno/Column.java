package com.pithy.free.anno;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    // 字段映射名
    String name() default "";

    // 是否忽略
    boolean ignore() default false;

}