package com.pithy.free.crud.domain;

import com.pithy.free.pageable.Pagination;

import java.util.List;

public class CaseObject {

    private String sqlpart;
    private List<Object> argpart;
    private Pagination pagination;

    public CaseObject() {

    }

    public CaseObject(String sqlpart, List<Object> argpart) {
        this.sqlpart = sqlpart;
        this.argpart = argpart;
    }

    public CaseObject(String sqlpart, List<Object> argpart, Pagination pagination) {
        this.sqlpart = sqlpart;
        this.argpart = argpart;
        this.pagination = pagination;
    }

    public String getSqlpart() {
        return sqlpart;
    }

    public void setSqlpart(String sqlpart) {
        this.sqlpart = sqlpart;
    }

    public List<Object> getArgpart() {
        return argpart;
    }

    public void setArgpart(List<Object> argpart) {
        this.argpart = argpart;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

}
