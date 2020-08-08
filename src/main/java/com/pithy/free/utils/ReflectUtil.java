package com.pithy.free.utils;

import com.pithy.free.anno.Column;
import com.pithy.free.anno.Table;
import com.pithy.free.crud.domain.ColumnObject;
import com.pithy.free.crud.domain.TableObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ReflectUtil {

    public static Object getFieldValue(Object owner, String fieldName) throws SecurityException, NoSuchMethodException,
            IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (fieldName == null || fieldName.length() == 0) {
            return null;
        }
        fieldName = new StringBuffer("get").append(fieldName.substring(0, 1).toUpperCase())
                .append(fieldName.substring(1)).toString();
        return owner.getClass().getMethod(fieldName).invoke(owner);
    }

//    public static String getTableValue(Object obj) throws Exception {
//        try {
//            String result = obj.getClass().getAnnotation(Table.class).name();
//            if (result == null || result.length() == 0) {
//                throw new Exception("invalid table name");
//            }
//            return result;
//        } catch (NullPointerException e) {
//            throw new Exception("invalid table annotation");
//        }
//    }

    public static TableObject getTableValue(Object clz) throws Exception {
        try {
            Table ann = clz.getClass().getAnnotation(Table.class);
            String table = ann.name();
            if (table == null || table.length() == 0) {
                throw new Exception("invalid table name");
            }
            String pk = ann.pk();
            if (pk == null || pk.length() == 0) {
                throw new Exception("invalid table pk name");
            }
            return new TableObject(table, pk);
        } catch (NullPointerException e) {
            throw new Exception("invalid table annotation");
        }
    }

    public static ColumnObject getColumnValue(Field f) throws NullPointerException {
        String fname = f.getName();
        if (fname.equals("id")) {
            return new ColumnObject(fname, false);
        }
        try {
            Column column = f.getAnnotation(Column.class);
            String result = column.name();
            if (result == null || result.length() == 0) {
                result = fname;
            }
            return new ColumnObject(result, column.ignore());
        } catch (NullPointerException e) {
            return new ColumnObject(fname, false);
        }
    }

}

