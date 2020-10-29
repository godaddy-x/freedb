package com.pithy.free.sqlcode.utils;

public class StringUtils {

    private static final String mobile_rgx = "^1[3|4|5|7|8][0-9]\\d{8}$";

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static boolean notEmpty(Object str) {
        return !isEmpty(str);
    }

    public static String[] split(String data, String sep) {
        if (StringUtils.isEmpty(data) || StringUtils.isEmpty(sep)) {
            return new String[]{};
        }
        return data.split(sep);
    }

    public static boolean isMobile(String mobile) {
        if (isEmpty(mobile)) {
            return false;
        }
        return mobile.matches(mobile_rgx);
    }

}
