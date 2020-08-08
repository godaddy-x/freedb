package com.pithy.free.crud.domain;

public class ColumnObject {

    private String name;
    private boolean ignore;
    private Object result;

    public ColumnObject() {

    }

    public ColumnObject(String name, boolean ignore) {
        this.name = name;
        this.ignore = ignore;
    }

    public ColumnObject(String name, boolean ignore, Object result) {
        this.name = name;
        this.ignore = ignore;
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
