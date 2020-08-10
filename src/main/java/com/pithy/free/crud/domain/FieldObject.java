package com.pithy.free.crud.domain;

import java.lang.reflect.Field;

public class FieldObject {

    private Field field;
    private Class clazz;

    public FieldObject() {

    }

    public FieldObject(Field field, Class clazz) {
        this.field = field;
        this.clazz = clazz;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
