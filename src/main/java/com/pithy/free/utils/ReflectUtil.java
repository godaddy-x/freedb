package com.pithy.free.utils;

import com.pithy.free.crud.domain.FieldObject;
import com.pithy.free.crud.domain.TableObject;
import com.pithy.free.crud.ex.DbEx;
import com.pithy.free.scan.TableClassLoader;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtil {

    public static Object getFieldValue(Object obj, Class clz, String field) throws Exception {
        return new PropertyDescriptor(field, clz).getReadMethod().invoke(obj);
    }

    public static List<FieldObject> getFieldObjects(Object object) {
        Class clazz = object.getClass();
        List<FieldObject> fieldList = new ArrayList<>();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                fieldList.add(new FieldObject(field, clazz));
            }
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }

    public static TableObject getTableValue(Object obj) throws DbEx {
        return TableClassLoader.getTableObject(obj);
    }

}

