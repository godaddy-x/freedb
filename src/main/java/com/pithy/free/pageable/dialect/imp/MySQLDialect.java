package com.pithy.free.pageable.dialect.imp;

import com.pithy.free.pageable.dialect.Dialect;

public class MySQLDialect implements Dialect {

    public MySQLDialect() {
    }

    public boolean supportsLimitOffset() {
        return true;
    }

    public boolean supportsLimit() {
        return true;
    }

    public String getLimitString(String sql, int offset, int limit) {
        return getLimitString(sql, offset, Integer.toString(offset), limit, Integer.toString(limit));
    }

    public String getLimitString(String sql, int offset, String offsetPlaceholder, int limit, String limitPlaceholder) {
        return (new StringBuilder()).append(sql).append(" limit ").append((offset - 1) * limit).append(",").append(limit).toString();
    }

    @Override
    public boolean supportLimit() {
        return true;
    }

    @Override
    public boolean supportOffset() {
        return true;
    }

    @Override
    public String getLimitSql(String sql, long pageNo, long pageSize) {
        return (new StringBuilder()).append(sql).append(" limit ").append((pageNo - 1) * pageSize).append(",").append(pageSize).toString();
    }

    @Override
    public String getOffsetSql(String sql, long offset, long limit) {
        return (new StringBuilder()).append(sql).append(" limit ").append(offset).append(",").append(limit).toString();
    }

    @Override
    public String getCountSql(String sql) {
        return new StringBuffer().append("select count(1) from (").append(sql).append(") as x").toString();
    }
}