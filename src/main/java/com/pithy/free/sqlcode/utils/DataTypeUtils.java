package com.pithy.free.sqlcode.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DataTypeUtils {

    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
    final static DateTimeFormatter sdf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    final static DateTimeFormatter sdf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String[] objectToStringArr(Object... argpart) {
        String[] param = new String[argpart.length];
        for (int i = 0; i < argpart.length; i++) {
            Object arg = argpart[i];
            String ret = null;
            Class clz = arg.getClass();
            if (clz == String.class) {
                ret = String.valueOf(arg);
            } else if (clz == Long.class) {
                ret = String.valueOf(arg);
            } else if (clz == Integer.class) {
                ret = String.valueOf(arg);
            } else if (clz == Short.class) {
                ret = String.valueOf(arg);
            } else if (clz == Float.class) {
                ret = String.valueOf(arg);
            } else if (clz == Double.class) {
                ret = String.valueOf(arg);
            } else if (clz == Character.class) {
                ret = String.valueOf(arg);
            } else if (clz == Byte.class) {
                ret = String.valueOf(arg);
            } else if (clz == Date.class) {
                Date date = (Date) arg;
                ret = sdf.format(date);
            } else if (clz == java.sql.Date.class) {
                java.sql.Date date = (java.sql.Date) arg;
                ret = sdf.format(date);
            } else if (clz == LocalDateTime.class) {
                LocalDateTime date = (LocalDateTime) arg;
                ret = sdf2.format(date);
            } else if (clz == LocalDate.class) {
                LocalDate date = (LocalDate) arg;
                ret = sdf3.format(date);
            }
            param[i] = ret;
        }
        return param;
    }

}
