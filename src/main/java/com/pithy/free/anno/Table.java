package com.pithy.free.anno;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

    // 表名
    String name() default "";

    // 主键名
    String pk() default "id";

    // 主键生成方式 SNOW.雪花ID AUTO.数据库自增
    IdGen type() default IdGen.SNOW;

}