package com.pithy.free.crud.domain;

public class DBConfig {

    private boolean snowflakeID; // 雪花ID填充 false.数据库生成 true.程序层填充
    private long slowQuery; // SQL耗时差值/毫秒,0.不开启

    public DBConfig() {

    }

    public DBConfig(boolean snowflakeID) {
        this(snowflakeID, 0);
    }

    public DBConfig(long slowQuery) {
        this(false, slowQuery);
    }

    public DBConfig(boolean snowflakeID, long slowQuery) {
        this.snowflakeID = snowflakeID;
        this.slowQuery = slowQuery;
    }

    public boolean isSnowflakeID() {
        return snowflakeID;
    }

    public void setSnowflakeID(boolean snowflakeID) {
        this.snowflakeID = snowflakeID;
    }

    public long getSlowQuery() {
        return slowQuery;
    }

    public void setSlowQuery(long slowQuery) {
        this.slowQuery = slowQuery;
    }

}
