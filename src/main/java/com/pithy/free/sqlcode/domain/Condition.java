package com.pithy.free.sqlcode.domain;

import com.pithy.free.sqlcode.aconst.OPT;

import java.util.List;

public class Condition<T> {

    private OPT logic;
    private String key;
    private T value;
    private List<T> values;

    public Condition() {

    }

    public Condition(OPT logic, String key, T value) {
        this.logic = logic;
        this.key = key;
        this.value = value;
    }

    public Condition(OPT logic, String key, List<T> values) {
        this.logic = logic;
        this.key = key;
        this.values = values;
    }

    public OPT getLogic() {
        return logic;
    }

    public void setLogic(OPT logic) {
        this.logic = logic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public List<T> getValues() {
        return values;
    }

    public void setValues(List<T> values) {
        this.values = values;
    }

}
