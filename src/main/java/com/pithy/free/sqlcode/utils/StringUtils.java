package com.pithy.free.sqlcode.utils;

public class StringUtils {

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean notEmpty(Object str) {
        return !isEmpty(str);
    }

}
