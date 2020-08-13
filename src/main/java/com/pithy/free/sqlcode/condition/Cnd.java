package com.pithy.free.sqlcode.condition;

import com.pithy.free.pageable.Pagination;
import com.pithy.free.sqlcode.aconst.SortBy;
import com.pithy.free.sqlcode.domain.Condition;
import com.pithy.free.sqlcode.domain.Join;
import com.pithy.free.sqlcode.domain.OrderBy;

import java.util.List;
import java.util.Map;

public interface Cnd {

    // =
    public Cnd eq(String key, Object value);

    // <>
    public Cnd noteq(String key, Object value);

    // <
    public Cnd lt(String key, Object value);

    // <=
    public Cnd lte(String key, Object value);

    // >
    public Cnd gt(String key, Object value);

    // >=
    public Cnd gte(String key, Object value);

    // is null
    public Cnd isnull(String key);

    // is not null
    public Cnd notnull(String key);

    // between x and y
    public Cnd between(String key, Object value1, Object value2);

    // between time, >= a b <
    public Cnd betweenDate(String key, Object value1, Object value2);

    // not between x and y
    public Cnd notbetween(String key, Object value1, Object value2);

    // in
    public Cnd in(String key, Object... values);

    // in
    public Cnd in(String key, Object values);

    // not in
    public Cnd notin(String key, Object... values);

    // not in
    public Cnd notin(String key, Object values);

    // like
    public Cnd like(String key, Object value);

    // not like
    public Cnd notlike(String key, Object value);

    // or
    public Cnd or(Cnd... cnds);

    // 分页方法(根据数据库方言)
    public Cnd limit(Long pageSize);

    // 分页方法(根据数据库方言)
    public Cnd limit(Long pageNo, Long pageSize);

    // 分页方法(根据数据库方言)
    public Cnd limit(Long pageNo, Long pageSize, boolean spilled);

    // 分页方法(根据数据库方言)
    public Cnd offset(Long offset, Long limit);

    // group by
    public Cnd groupby(String... keys);

    // order by
    public Cnd orderby(String key, SortBy sortBy);

    // order by
    public Cnd orderby(OrderBy... orderBy);

    // 获取指定字段
    public Cnd fields(String... keys);

    // 指定更新字段
    public Cnd upset(String[] keys, Object... values);

    // 左连接表
    public Cnd leftJoin(Class entity, String alias, String join);

    // 左连接表
    public Cnd leftJoin(Cnd entity, String alias, String join);

    // 右连接表
    public Cnd rightJoin(Class entity, String alias, String join);

    // 右连接表
    public Cnd rightJoin(Cnd entity, String alias, String join);

    // 内连接表
    public Cnd innerJoin(Class entity, String alias, String join);

    // 内连接表
    public Cnd innerJoin(Cnd entity, String alias, String join);

    // 复制条件
    public Cnd copy(Cnd cnd);

    public List<Condition<?>> getConditions();

    public Pagination<Object> getPagination();

    public void setPagination(Pagination<Object> pagination);

    public Class getEntityClass();

    public List<String> getGroupbys();

    public List<String> getFields();

    public List<OrderBy> getOrderbys();

    public Map<String, Object> getUpsets();

    public String getEntityAlias();

    public List<Join> getJoins();

}
