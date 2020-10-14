package com.pithy.free.crud.domain;

import com.pithy.free.anno.IdGen;

import java.util.List;

public class TableObject {

    private String tableName; // 数据库表名
    private String pkName; // 数据库主键名
    private Integer pkType; // 数据库主键类型 0.Long 1.string
    private IdGen idGen; // 数据库主键生成方式
    private ColumnObject pkColumn; // 数据库主键列对象
    private Class modelClass; // 实体类型
    private String modelClassName; // 实体类路径
    private List<ColumnObject> columnObjects; // 数据库实体列对象

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

    public Integer getPkType() {
        return pkType;
    }

    public void setPkType(Integer pkType) {
        this.pkType = pkType;
    }

    public IdGen getIdGen() {
        return idGen;
    }

    public void setIdGen(IdGen idGen) {
        this.idGen = idGen;
    }

    public ColumnObject getPkColumn() {
        return pkColumn;
    }

    public void setPkColumn(ColumnObject pkColumn) {
        this.pkColumn = pkColumn;
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
