package com.pithy.free.crud.domain;

public class TableObject {

    private String tableName;
    private String pkName;

    public TableObject() {

    }

    public TableObject(String tableName, String pkName) {
        this.tableName = tableName;
        this.pkName = pkName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

}
