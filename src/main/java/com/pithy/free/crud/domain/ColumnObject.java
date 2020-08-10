package com.pithy.free.crud.domain;

public class ColumnObject {

    private String mdName; // 实体字段名
    private String dbName; // 数据库字段名
    private boolean ignore; // 是否跳过
    private boolean isPK; // 是否主键
    private Class classType; // 字段所属类
    private Class fieldType; // 字段类型
    private String QueryPart; // 拼接查询sql部分

    private Object result;

    public ColumnObject() {

    }

    public String getMdName() {
        return mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
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

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public boolean isPK() {
        return isPK;
    }

    public void setPK(boolean PK) {
        isPK = PK;
    }

    public Class getClassType() {
        return classType;
    }

    public void setClassType(Class classType) {
        this.classType = classType;
    }

    public String getQueryPart() {
        return QueryPart;
    }

    public void setQueryPart(String queryPart) {
        QueryPart = queryPart;
    }

    public Class getFieldType() {
        return fieldType;
    }

    public void setFieldType(Class fieldType) {
        this.fieldType = fieldType;
    }
}
