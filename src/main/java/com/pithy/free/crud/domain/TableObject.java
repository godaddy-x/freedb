package com.pithy.free.crud.domain;

import java.util.List;

public class TableObject {

    private String tableName; // 数据库表名
    private String pkName; // 数据库主键名
    private Class modelClass; // 实体类型
    private String modelClassName; // 实体类路径
    private List<ColumnObject> columnObjects; // 数据库实体列对象

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

    public Class getModelClass() {
        return modelClass;
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }

    public String getModelClassName() {
        return modelClassName;
    }

    public void setModelClassName(String modelClassName) {
        this.modelClassName = modelClassName;
    }

    public List<ColumnObject> getColumnObjects() {
        return columnObjects;
    }

    public void setColumnObjects(List<ColumnObject> columnObjects) {
        this.columnObjects = columnObjects;
    }

}
