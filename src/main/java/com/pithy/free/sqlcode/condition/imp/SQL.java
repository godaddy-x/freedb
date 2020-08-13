package com.pithy.free.sqlcode.condition.imp;

import com.pithy.free.crud.aconst.MODE;
import com.pithy.free.crud.domain.IDEntity;
import com.pithy.free.pageable.Pagination;
import com.pithy.free.pageable.imp.SimplePagination;
import com.pithy.free.sqlcode.aconst.OPT;
import com.pithy.free.sqlcode.aconst.SortBy;
import com.pithy.free.sqlcode.condition.Cnd;
import com.pithy.free.sqlcode.domain.Condition;
import com.pithy.free.sqlcode.domain.Join;
import com.pithy.free.sqlcode.domain.OrderBy;
import com.pithy.free.sqlcode.utils.StringUtils;

import java.util.*;

public class SQL implements Cnd {

    protected Class entityClass; // 主表对象
    protected String entityAlias; // 主表对象别名
    protected List<Condition<?>> conditions = new ArrayList<>();
    protected List<String> fields = new ArrayList<>();
    protected List<String> groupbys = new ArrayList<>();
    protected List<OrderBy> orderbys = new ArrayList<>();
    protected Map<String, Object> upsets = new HashMap<>();
    protected List<Join> joins = new ArrayList<>();
    private Pagination<Object> pagination;

    public SQL() {

    }

    public SQL(Class entityClass) {
        this(entityClass, null);
    }

    public SQL(Class entityClass, String alias) {
        this.entityClass = entityClass;
        this.entityAlias = alias;
    }

    @Override
    public Cnd eq(String key, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.EQ, key, value));
        return this;
    }

    @Override
    public Cnd noteq(String key, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.NOT_EQ, key, value));
        return this;
    }

    @Override
    public Cnd lt(String key, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.LT, key, value));
        return this;
    }

    @Override
    public Cnd lte(String key, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.LTE, key, value));
        return this;
    }

    @Override
    public Cnd gt(String key, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.GT, key, value));
        return this;
    }

    @Override
    public Cnd gte(String key, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.GTE, key, value));
        return this;
    }

    @Override
    public Cnd isnull(String key) {
        if (StringUtils.isEmpty(key)) {
            return this;
        }
        conditions.add(new Condition<Object>(OPT.IS_NULL, key, ""));
        return this;
    }

    @Override
    public Cnd notnull(String key) {
        if (StringUtils.isEmpty(key)) {
            return this;
        }
        conditions.add(new Condition<Object>(OPT.IS_NOT_NULL, key, ""));
        return this;
    }

    @Override
    public Cnd between(String key, Object value1, Object value2) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.BETWEEN, key, Arrays.asList(value1, value2)));
        return this;
    }

    @Override
    public Cnd betweenDate(String key, Object value1, Object value2) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.BETWEEN2, key, Arrays.asList(value1, value2)));
        return this;
    }

    @Override
    public Cnd notbetween(String key, Object value1, Object value2) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value1) || StringUtils.isEmpty(value2)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.NOT_BETWEEN, key, Arrays.asList(value1, value2)));
        return this;
    }

    @Override
    public Cnd in(String key, Object... values) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(values) || values.length == 0) {
            return this;
        }
        if (values.length == 1 && values[0] instanceof List<?>) {
            conditions.add(new Condition<>(OPT.IN, key, (List<Object>) values[0]));
        } else {
            conditions.add(new Condition<>(OPT.IN, key, Arrays.asList(values)));
        }
        return this;
    }

    @Override
    public Cnd in(String key, Object values) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(values)) {
            return this;
        }
        if (values instanceof List<?>) {
            List<Object> list = (List<Object>) values;
            if (list.size() == 0) {
                return this;
            }
            conditions.add(new Condition<>(OPT.IN, key, list));
        }
        return this;
    }

    @Override
    public Cnd notin(String key, Object... values) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(values) || values.length == 0) {
            return this;
        }
        if (values.length == 1 && values[0] instanceof List<?>) {
            conditions.add(new Condition<>(OPT.NOT_IN, key, (List<Object>) values[0]));
        } else {
            conditions.add(new Condition<>(OPT.NOT_IN, key, Arrays.asList(values)));
        }
        return this;
    }

    @Override
    public Cnd notin(String key, Object values) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(values)) {
            return this;
        }
        if (values instanceof List<?>) {
            List<Object> list = (List<Object>) values;
            if (list.size() == 0) {
                return this;
            }
            conditions.add(new Condition<>(OPT.NOT_IN, key, list));
        }
        return this;
    }

    @Override
    public Cnd like(String key, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.LIKE, key, value));
        return this;
    }

    @Override
    public Cnd notlike(String key, Object value) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return this;
        }
        conditions.add(new Condition<>(OPT.NOT_LIKE, key, value));
        return this;
    }

    @Override
    public Cnd or(Cnd... cnds) {
        if (StringUtils.isEmpty(cnds) || cnds.length == 0) {
            return this;
        }
        List<Object> values = new LinkedList<>();
        for (Cnd cnd : cnds) {
            values.add(cnd);
        }
        conditions.add(new Condition<>(OPT.OR, "", values));
        return this;
    }

    @Override
    public Cnd limit(Long pageSize) {
        if (StringUtils.isEmpty(pageSize)) {
            return this;
        }
        return limit(1l, pageSize, true);
    }

    @Override
    public Cnd limit(Long pageNo, Long pageSize) {
        return limit(pageNo, pageSize, true);
    }

    @Override
    public Cnd limit(Long pageNo, Long pageSize, boolean spilled) {
        if (StringUtils.isEmpty(pageNo) && StringUtils.isEmpty(pageSize)) {
            return this;
        }
        if (pageNo == null || pageNo < 1) {
            pageNo = 1l;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 500) {
            pageSize = 50l;
        }
        pagination = new SimplePagination<>(pageNo, pageSize, spilled);
        return this;
    }

    @Override
    public Cnd offset(Long pageNo, Long pageSize) {
        if (StringUtils.isEmpty(pageNo) && StringUtils.isEmpty(pageSize)) {
            return this;
        }
        if (pageNo == null || pageNo < 0) {
            pageNo = 0l;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 500) {
            pageSize = 50l;
        }
        pagination = new SimplePagination<>(pageNo, pageSize, false);
        pagination.setOffset(true);
        return this;
    }

    @Override
    public Cnd groupby(String... keys) {
        if (StringUtils.isEmpty(keys) || keys.length == 0) {
            return this;
        }
        for (String object : keys) {
            groupbys.add(object);
        }
        return this;
    }

    @Override
    public Cnd orderby(String key, SortBy sortBy) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(sortBy)) {
            return this;
        }
        orderbys.add(new OrderBy(key, sortBy));
        return this;
    }

    @Override
    public Cnd orderby(OrderBy... orderBys) {
        if (StringUtils.isEmpty(orderBys) || orderBys.length == 0) {
            return this;
        }
        for (OrderBy orderBy : orderBys) {
            orderbys.add(new OrderBy(orderBy.getKey(), orderBy.getValue()));
        }
        return this;
    }

    @Override
    public Cnd fields(String... keys) {
        if (StringUtils.isEmpty(keys) || keys.length == 0) {
            return this;
        }
        for (String object : keys) {
            fields.add(object);
        }
        return this;
    }

    @Override
    public List<Condition<?>> getConditions() {
        return conditions;
    }

    @Override
    public Pagination<Object> getPagination() {
        return pagination;
    }

    @Override
    public void setPagination(Pagination<Object> pagination) {
        this.pagination = pagination;
    }

    @Override
    public Cnd upset(String[] keys, Object... values) {
        if (StringUtils.isEmpty(keys) || keys.length == 0) {
            return this;
        }
        if (StringUtils.isEmpty(values) || values.length == 0) {
            return this;
        }
        if (keys.length != values.length) {
            return this;
        }
        for (int i = 0; i < keys.length; i++) {
            upsets.put(keys[i], values[i]);
        }
        return this;
    }

    @Override
    public Cnd leftJoin(Class entity, String alias, String join) {
        if (StringUtils.isEmpty(entity) || StringUtils.isEmpty(alias) || StringUtils.isEmpty(join)) {
            return this;
        }
        joins.add(new Join(MODE.LEFT, entity, alias, join));
        return this;
    }

    @Override
    public Cnd leftJoin(Cnd entity, String alias, String join) {
        if (StringUtils.isEmpty(entity) || StringUtils.isEmpty(alias) || StringUtils.isEmpty(join)) {
            return this;
        }
        joins.add(new Join(MODE.LEFT, entity, alias, join));
        return this;
    }

    @Override
    public Cnd rightJoin(Class entity, String alias, String join) {
        if (StringUtils.isEmpty(entity) || StringUtils.isEmpty(alias) || StringUtils.isEmpty(join)) {
            return this;
        }
        joins.add(new Join(MODE.RIGHT, entity, alias, join));
        return this;
    }

    @Override
    public Cnd rightJoin(Cnd entity, String alias, String join) {
        if (StringUtils.isEmpty(entity) || StringUtils.isEmpty(alias) || StringUtils.isEmpty(join)) {
            return this;
        }
        joins.add(new Join(MODE.RIGHT, entity, alias, join));
        return this;
    }

    @Override
    public Cnd innerJoin(Class entity, String alias, String join) {
        if (StringUtils.isEmpty(entity) || StringUtils.isEmpty(alias) || StringUtils.isEmpty(join)) {
            return this;
        }
        joins.add(new Join(MODE.INNER, entity, alias, join));
        return this;
    }

    @Override
    public Cnd innerJoin(Cnd entity, String alias, String join) {
        if (StringUtils.isEmpty(entity) || StringUtils.isEmpty(alias) || StringUtils.isEmpty(join)) {
            return this;
        }
        joins.add(new Join(MODE.INNER, entity, alias, join));
        return this;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public List<String> getGroupbys() {
        return groupbys;
    }

    public List<OrderBy> getOrderbys() {
        return orderbys;
    }

    public List<String> getFields() {
        return fields;
    }

    public Map<String, Object> getUpsets() {
        return upsets;
    }

    public String getEntityAlias() {
        return entityAlias;
    }

    public List<Join> getJoins() {
        return joins;
    }

    @Override
    public Cnd copy(Cnd cnd) {
        if (cnd instanceof SQL) {
            SQL oldSql = (SQL) cnd;
            entityClass = oldSql.entityClass;
            entityAlias = oldSql.entityAlias;
            conditions.addAll(oldSql.conditions);
            fields.addAll(oldSql.fields);
            groupbys.addAll(oldSql.groupbys);
            orderbys.addAll(oldSql.orderbys);
            upsets.putAll(oldSql.upsets);
            joins.addAll(oldSql.joins);
            if (oldSql.pagination != null) {
                pagination = new SimplePagination<>();
                pagination.setOffset(oldSql.pagination.isOffset());
                pagination.setPageNumber(oldSql.pagination.getPageNumber());
                pagination.setPageTotal(oldSql.pagination.getPageTotal());
                pagination.setPageSize(oldSql.pagination.getPageSize());
                pagination.setPageNo(oldSql.pagination.getPageNo());
                pagination.setData(oldSql.pagination.getData());
            }
        }
        return this;
    }

}
