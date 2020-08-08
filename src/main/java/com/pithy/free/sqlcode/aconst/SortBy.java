package com.pithy.free.sqlcode.aconst;

/**
 * 排序定值枚举
 *
 * @author shadow
 */
public enum SortBy {

    A("asc"), D("desc");

    private SortBy(String value) {
        this.value = value;
    }

    private String value;

    public String toString() {
        return value;
    }

}
