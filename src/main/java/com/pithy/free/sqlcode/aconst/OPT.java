package com.pithy.free.sqlcode.aconst;

/**
 * 排序定值枚举
 *
 * @author shadow
 */
public enum OPT {

    EQ(" = "),
    NOT_EQ(" <> "),
    LT(" < "),
    LTE(" <= "),
    GT(" > "),
    GTE(" >= "),
    IS_NULL(" IS NULL "),
    IS_NOT_NULL(" IS NOT NULL "),
    BETWEEN(" BETWEEN "),
    BETWEEN2(" BETWEEN "),
    NOT_BETWEEN(" NOT BETWEEN "),
    IN(" IN "),
    NOT_IN(" NOT IN "),
    LIKE(" LIKE "),
    NOT_LIKE(" NOT LIKE "),
    OR(" OR "),
    ORDERBY(" ORDER BY ");

    private OPT(String value) {
        this.value = value;
    }

    private String value;

    public String toString() {
        return value;
    }

}
