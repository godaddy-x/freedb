package com.pithy.free.sqlcode.domain;

import com.pithy.free.sqlcode.aconst.SortBy;

public class OrderBy {

    private String key; // 排序字段
    private SortBy value; // 0.asc 1.desc

    public OrderBy() {

    }

    public OrderBy(String key, SortBy value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SortBy getValue() {
        return value;
    }

    public void setValue(SortBy value) {
        this.value = value;
    }

}
