package com.pithy.free.pageable.dialect;

public interface Dialect {

    public boolean supportLimit();

    public boolean supportOffset();

    public String getLimitSql(String sql, long pageNo, long pageSize);

	public String getOffsetSql(String sql, long offset, long limit);

	public String getCountSql(String sql);

}