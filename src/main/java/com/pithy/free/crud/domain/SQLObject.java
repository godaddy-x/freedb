package com.pithy.free.crud.domain;

import com.pithy.free.pageable.Pagination;

public class SQLObject<E> {

    private String sqlstr;
    private Object[] sqlarg;
    private Pagination<E> pagination;

    public SQLObject() {

    }

    public SQLObject(String sqlstr, Object[] sqlarg, Pagination<E> pagination) {
        this.sqlstr = sqlstr;
        this.sqlarg = sqlarg;
        this.pagination = pagination;
    }

    public String getSqlstr() {
        return sqlstr;
    }

    public void setSqlstr(String sqlstr) {
        this.sqlstr = sqlstr;
    }

    public Object[] getSqlarg() {
        return sqlarg;
    }

    public void setSqlarg(Object[] sqlarg) {
        this.sqlarg = sqlarg;
    }

    public Pagination<E> getPagination() {
        return pagination;
    }

    public void setPagination(Pagination<E> pagination) {
        this.pagination = pagination;
    }
}
