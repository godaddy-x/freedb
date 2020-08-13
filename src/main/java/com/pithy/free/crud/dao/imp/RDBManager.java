package com.pithy.free.crud.dao.imp;

import com.pithy.free.crud.aconst.MODE;
import com.pithy.free.crud.dao.IDBase;
import com.pithy.free.crud.domain.*;
import com.pithy.free.crud.ex.DbEx;
import com.pithy.free.pageable.Pagination;
import com.pithy.free.pageable.dialect.Dialect;
import com.pithy.free.pageable.dialect.imp.MySQLDialect;
import com.pithy.free.pageable.imp.SimplePagination;
import com.pithy.free.sqlcode.aconst.OPT;
import com.pithy.free.sqlcode.condition.Cnd;
import com.pithy.free.sqlcode.condition.imp.SQL;
import com.pithy.free.sqlcode.domain.Condition;
import com.pithy.free.sqlcode.domain.Join;
import com.pithy.free.sqlcode.domain.OrderBy;
import com.pithy.free.sqlcode.utils.DataTypeUtils;
import com.pithy.free.sqlcode.utils.StringUtils;
import com.pithy.free.utils.IdWorker;
import com.pithy.free.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * mysql数据库 - CRUD封装实现类
 *
 * @author shadow
 */
public class RDBManager implements IDBase {

    private static final Logger log = LoggerFactory.getLogger(RDBManager.class);

    private DBConfig dbConfig = new DBConfig();

    private JdbcTemplate template;

    public JdbcTemplate getTemplate() {
        return template;
    }

    public void setTemplate(JdbcTemplate template) {
        this.template = template;
    }

    public DBConfig getDbConfig() {
        return dbConfig;
    }

    public void setDbConfig(DBConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    public RDBManager() {

    }

    public RDBManager(JdbcTemplate template) {
        this.template = template;
    }

    public RDBManager(JdbcTemplate template, DBConfig dbConfig) {
        this.template = template;
        this.dbConfig = dbConfig;
    }

    @Override
    public int save(IDEntity... entities) throws DbEx {
        if (entities == null) {
            throw new DbEx("invalid entity null");
        }
        if (entities.length == 0 || entities.length > 1000) {
            throw new DbEx("invalid entity len range 1-1000");
        }
        String sqlstr = null;
        TableObject table = null;
        List<Object[]> argpart = new ArrayList<>(entities.length);
        StringBuffer part1 = new StringBuffer();
        StringBuffer part2 = new StringBuffer();
        try {
            for (int i = 0; i < entities.length; i++) {
                IDEntity entity = entities[i];
                if (table == null) {
                    table = ReflectUtil.getTableValue(entity);
                }
                List<ColumnObject> fields = table.getColumnObjects();
                List<Object> arg = new ArrayList<>(fields.size());
                for (ColumnObject column : fields) {
                    Object columnValue = ReflectUtil.getFieldValue(entity, column.getClassType(), column.getMdName());
                    if (column.isPK()) { // PK字段
                        if (!dbConfig.isSnowflakeID()) { // 自动填充ID字段跳过
                            continue;
                        }
                        if (columnValue == null) {
                            if (Long.class == column.getFieldType()) {
                                columnValue = IdWorker.getLID();
                            } else if (String.class == column.getFieldType()) {
                                columnValue = IdWorker.getSID();
                            } else {
                                throw new DbEx("invalid entity id type");
                            }
                        }
                    }
                    if (i == 0) {
                        part1.append(column.getDbName()).append(",");
                        part2.append("?,");
                    }
                    arg.add(columnValue);
                }
                argpart.add(arg.toArray());
            }
            if (part1.length() == 0) {
                throw new DbEx("invalid entity field len");
            }
            sqlstr = new StringBuffer().append("insert into ").append(table.getTableName()).append(" (").
                    append(part1.substring(0, part1.length() - 1)).append(") values (")
                    .append(part2.substring(0, part2.length() - 1)).append(")").toString();
            int[] ret = template.batchUpdate(sqlstr, argpart);
            if (ret != null) {
                return ret.length;
            }
            return 0;
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            logSqlHandle(0, sqlstr, argpart.toArray());
        }
    }

    @Override
    public int save(List entities) throws DbEx {
        return save(listToArr(entities));
    }

    @Override
    public int update(IDEntity... entities) throws DbEx {
        if (entities == null) {
            throw new DbEx("invalid entity null");
        }
        if (entities.length == 0 || entities.length > 1000) {
            throw new DbEx("invalid entity len range 1-1000");
        }
        String sqlstr = null;
        List<Object> arglist = new ArrayList<>();
        int ret = 0;
        try {
            for (int i = 0; i < entities.length; i++) {
                StringBuffer part1 = new StringBuffer();
                StringBuffer part2 = new StringBuffer();
                IDEntity entity = entities[i];
                TableObject table = ReflectUtil.getTableValue(entity);
                Object pkValue = null;
                List<ColumnObject> fields = table.getColumnObjects();
                List<Object> argpart = new ArrayList<>(fields.size());
                for (ColumnObject column : fields) {
                    Object columnValue = ReflectUtil.getFieldValue(entity, column.getClassType(), column.getMdName());
                    if (column.isPK()) { // 主键字段为空抛出异常
                        if (columnValue == null) {
                            throw new NullPointerException("invalid pk value");
                        }
                        pkValue = columnValue;
                        part2.append(column.getDbName()).append("=?");
                    } else {
                        if (columnValue == null) {
                            continue;
                        }
                        part1.append(column.getDbName()).append("=?,");
                        argpart.add(columnValue);
                        arglist.add(columnValue);
                    }
                }
                if (part1.length() == 0) {
                    throw new DbEx("invalid entity field len");
                }
                argpart.add(pkValue);
                sqlstr = new StringBuffer().append("update ").append(table.getTableName()).append(" set ")
                        .append(part1.substring(0, part1.length() - 1)).append(" where ").append(part2).toString();
                if (template.update(sqlstr, argpart.toArray()) > 0) {
                    ret++;
                }
            }
            return ret;
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            logSqlHandle(0, sqlstr, arglist.toArray());
        }
    }

    @Override
    public int update(List entities) throws DbEx {
        return update(listToArr(entities));
    }

    @Override
    public int update(Cnd cnd) throws DbEx {
        if (cnd == null || cnd.getEntityClass() == null) {
            throw new DbEx("invalid cnd or entity");
        }
        String sqlstr = null;
        Object[] argList = new Object[]{};
        try {
            StringBuffer part1 = new StringBuffer();
            TableObject table = ReflectUtil.getTableValue(cnd.getEntityClass());
            if (cnd.getUpsets().size() == 0) {
                throw new DbEx("invalid upset field");
            }
            if (cnd.getConditions().size() == 0) {
                throw new DbEx("invalid where case must than 0");
            }
            List<Object> argpart = new ArrayList<>(cnd.getUpsets().size());
            for (Map.Entry<String, Object> entry : cnd.getUpsets().entrySet()) {
                if (table.getPkName().equals(entry.getKey())) {
                    continue;
                }
                part1.append(entry.getKey()).append("=?,");
                argpart.add(entry.getValue());
            }
            if (part1.length() == 0) {
                throw new DbEx("invalid upset field len");
            }
            CaseObject whereCase = buildWhereCase(cnd);
            StringBuffer sqlpart = new StringBuffer();
            if (whereCase.getSqlpart().length() == 0) {
                throw new DbEx("invalid upset condition must than 0");
            }
            sqlstr = sqlpart.append("update ").append(table.getTableName()).append(" set ").append(part1.substring(0, part1.length() - 1)).append(" where").append(whereCase.getSqlpart().substring(0, whereCase.getSqlpart().length() - 3)).toString();
            argpart.addAll(whereCase.getArgpart());
            argList = argpart.toArray();
            return template.update(sqlstr, argList);
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            logSqlHandle(0, sqlstr, argList);
        }
    }

    @Override
    public int delete(IDEntity... entities) throws DbEx {
        if (entities == null) {
            throw new DbEx("invalid entity null");
        }
        if (entities.length == 0 || entities.length > 1000) {
            throw new DbEx("invalid entity len range 1-1000");
        }
        String sqlstr = null;
        TableObject table = null;
        StringBuffer part1 = new StringBuffer();
        List<Object> argList = new ArrayList<>(entities.length);
        try {
            for (int i = 0; i < entities.length; i++) {
                IDEntity entity = entities[i];
                if (table == null) {
                    table = ReflectUtil.getTableValue(entity);
                }
                ColumnObject pkColumn = table.getPkColumn();
                Object columnValue = ReflectUtil.getFieldValue(entity, pkColumn.getClassType(), pkColumn.getMdName());
                if (columnValue == null) {
                    throw new NullPointerException("invalid pk column value null");
                }
                part1.append("?,");
                argList.add(columnValue);

            }
            if (part1.length() == 0) {
                throw new DbEx("invalid entity field len");
            }
            sqlstr = new StringBuffer().append("delete from ").append(table.getTableName()).append(" where ").append(table.getPkName()).append(" in (").append(part1.substring(0, part1.length() - 1)).append(")").toString();
            return template.update(sqlstr, argList.toArray());
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            logSqlHandle(0, sqlstr, argList.toArray());
        }
    }

    @Override
    public int delete(List entities) throws DbEx {
        return delete(listToArr(entities));
    }

    @Override
    public int delete(Cnd cnd) throws DbEx {
        if (cnd == null || cnd.getEntityClass() == null) {
            throw new DbEx("invalid cnd or entity");
        }
        String sqlstr = null;
        Object[] argList = null;
        try {
            TableObject table = ReflectUtil.getTableValue(cnd.getEntityClass());
            if (cnd.getConditions().size() == 0) {
                throw new DbEx("invalid where case must than 0");
            }
            List<Object> argpart = new ArrayList<>(cnd.getUpsets().size());
            CaseObject whereCase = buildWhereCase(cnd);
            StringBuffer sqlpart = new StringBuffer();
            if (whereCase.getSqlpart().length() == 0) {
                throw new DbEx("invalid delete condition must than 0");
            }
            sqlstr = sqlpart.append("delete from ").append(table.getTableName()).append(" where").append(whereCase.getSqlpart().substring(0, whereCase.getSqlpart().length() - 3)).toString();
            argpart.addAll(whereCase.getArgpart());
            argList = argpart.toArray();
            return template.update(sqlstr, argList);
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            logSqlHandle(0, sqlstr, argList);
        }
    }

    @Override
    public <E> E findByPK(Object pkval, Class<E> mapper) throws DbEx {
        TableObject table = ReflectUtil.getTableValue(mapper);
        return findOne(new SQL(mapper).eq(table.getPkName(), pkval), mapper);
    }

    @Override
    public <E> E findOne(Cnd cnd) throws DbEx {
        return findOne(cnd, null);
    }

    @Override
    public <E> E findOne(Cnd cnd, Class<E> mapper) throws DbEx {
        List<E> list = findList(cnd.offset(0l, 1l), mapper);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public <E> List<E> findList(Cnd cnd) throws DbEx {
        return findList(cnd, null);
    }

    @Override
    public <E> List<E> findList(Cnd cnd, Class<E> mapper) throws DbEx {
        return findPage(cnd, mapper).getData();
    }

    @Override
    public <E> Pagination<E> findPage(Cnd cnd) throws DbEx {
        return findPage(cnd, null);
    }

    @Override
    public <E> Pagination<E> findPage(Cnd cnd, Class<E> mapper) throws DbEx {
        if (cnd == null || cnd.getEntityClass() == null) {
            throw new DbEx("invalid cnd or entity");
        }
        String sqlstr = null;
        Object[] argpart = null;
        long start = System.currentTimeMillis();
        try {
            StringBuffer part1 = new StringBuffer();
            TableObject table = ReflectUtil.getTableValue(cnd.getEntityClass());
            if (cnd.getFields().size() == 0) {
                for (ColumnObject column : table.getColumnObjects()) {
                    if (column.getDbName().equals(column.getMdName())) {
                        part1.append(column.getDbName()).append(",");
                    } else {
                        part1.append(column.getDbName()).append(" as ").append(column.getMdName()).append(",");
                    }
                }
            } else {
                for (String field : cnd.getFields()) {
                    if (field == null || field.trim().length() == 0) {
                        continue;
                    }
                    part1.append(field.trim()).append(",");
                }
            }
            if (part1.length() == 0) {
                throw new DbEx("invalid entity field len");
            }
            CaseObject whereCase = buildWhereCase(cnd);
            StringBuffer sqlpart = new StringBuffer();
            argpart = whereCase.getArgpart().toArray();
            sqlpart.append("select ").append(part1.substring(0, part1.length() - 1)).append(" from ").append(table.getTableName()).append(" ");
            if (whereCase.getSqlpart().length() > 0) {
                sqlpart.append("where").append(whereCase.getSqlpart().substring(0, whereCase.getSqlpart().length() - 3));
            }
            CaseObject groupbyCase = buildGroupbyCase(cnd);
            if (groupbyCase.getSqlpart().length() > 0) {
                sqlpart.append(groupbyCase.getSqlpart());
            }
            CaseObject sortbyCase = buildSortbyCase(cnd);
            if (sortbyCase.getSqlpart().length() > 0) {
                sqlpart.append(sortbyCase.getSqlpart());
            }
            Pagination<E> pagination = null;
            sqlstr = sqlpart.toString();
            CaseObject pageObj = buildPagination(cnd, sqlstr, argpart);
            if (pageObj != null) { // normal query list
                sqlstr = pageObj.getSqlpart();
                pagination = pageObj.getPagination();
            } else {
                pagination = new SimplePagination<>();
            }
            if (mapper == null) {
                mapper = cnd.getEntityClass();
            }
            List<E> list = null;
            if (IDEntity.class.isAssignableFrom(mapper)) {
                list = template.query(sqlstr, argpart, new BeanPropertyRowMapper<E>(mapper));
            } else {
                list = template.queryForList(sqlstr, mapper, argpart);
            }
            if (list == null) {
                list = new ArrayList<>();
            }
            pagination.setData(list);
            return pagination;
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            logSqlHandle(start, sqlstr, argpart);
        }
    }

    @Override
    public <E> E findOneComplex(Cnd cnd) throws DbEx {
        return findOneComplex(cnd, null);
    }

    @Override
    public <E> E findOneComplex(Cnd cnd, Class<E> mapper) throws DbEx {
        List<E> list = findListComplex(cnd.offset(0l, 1l), mapper);
        if (list == null || list.size() == 0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public <E> List<E> findListComplex(Cnd cnd) throws DbEx {
        return findListComplex(cnd, null);
    }

    @Override
    public <E> List<E> findListComplex(Cnd cnd, Class<E> mapper) throws DbEx {
        return findPageComplex(cnd, mapper).getData();
    }

    @Override
    public <E> Pagination<E> findPageComplex(Cnd cnd) throws DbEx {
        return findPageComplex(cnd, null);
    }

    @Override
    public <E> Pagination<E> findPageComplex(Cnd cnd, Class<E> mapper) throws DbEx {
        if (cnd == null || cnd.getEntityClass() == null) {
            throw new DbEx("invalid cnd or entity");
        }
        if (StringUtils.isEmpty(cnd.getEntityAlias())) {
            throw new DbEx("invalid entity alias");
        }
        SQLObject<E> sqlObject = null;
        long start = System.currentTimeMillis();
        try {
            sqlObject = buildSQLObject(cnd);
            if (mapper == null) {
                mapper = cnd.getEntityClass();
            }
            Pagination<E> pagination = sqlObject.getPagination();
            List<E> list = null;
            if (IDEntity.class.isAssignableFrom(mapper)) {
                list = template.query(sqlObject.getSqlstr(), sqlObject.getSqlarg(), new BeanPropertyRowMapper<>(mapper));
            } else {
                list = template.queryForList(sqlObject.getSqlstr(), mapper, sqlObject.getSqlarg());
            }
            if (list == null) {
                list = new ArrayList<>();
            }
            pagination.setData(list);
            return pagination;
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            if (sqlObject == null) {
                sqlObject = new SQLObject<>();
            }
            logSqlHandle(start, sqlObject.getSqlstr(), sqlObject.getSqlarg());
        }
    }

    private <E> SQLObject buildSQLObject(Cnd cnd) throws DbEx {
        String sqlstr = null;
        Object[] argpart = null;
        StringBuffer part1 = new StringBuffer();
        if (cnd.getFields().size() == 0) {
            throw new DbEx("invalid query field len");
        }
        for (String field : cnd.getFields()) {
            if (StringUtils.isEmpty(field)) {
                continue;
            }
            String key = field.trim();
            if (StringUtils.isEmpty(key)) {
                continue;
            }
            part1.append(key).append(",");
        }
        if (part1.length() == 0) {
            throw new DbEx("invalid entity field len");
        }
        TableObject table = ReflectUtil.getTableValue(cnd.getEntityClass());
        CaseObject whereCase = buildWhereCase(cnd);
        StringBuffer sqlpart = new StringBuffer();
//        argpart = whereCase.getArgpart().toArray();
        List<Object> sqlarg = new ArrayList<>();
        sqlpart.append("select ").append(part1.substring(0, part1.length() - 1)).append(" from ").append(table.getTableName()).append(" ").append(cnd.getEntityAlias()).append(" ");
        CaseObject joinCase = buildJoinCase(cnd);
        if (joinCase.getSqlpart().length() > 0) {
            sqlpart.append(joinCase.getSqlpart());
        }
        if (joinCase.getArgpart().size() > 0) {
            sqlarg.add(Arrays.asList(joinCase.getArgpart()));
        }
        if (whereCase.getSqlpart().length() > 0) {
            sqlpart.append("where").append(whereCase.getSqlpart().substring(0, whereCase.getSqlpart().length() - 3));
        }
        if (whereCase.getArgpart().size() > 0) {
            sqlarg.add(Arrays.asList(whereCase.getArgpart()));
        }
        CaseObject groupbyCase = buildGroupbyCase(cnd);
        if (groupbyCase.getSqlpart().length() > 0) {
            sqlpart.append(groupbyCase.getSqlpart());
        }
        CaseObject sortbyCase = buildSortbyCase(cnd);
        if (sortbyCase.getSqlpart().length() > 0) {
            sqlpart.append(sortbyCase.getSqlpart());
        }
        Pagination<E> pagination = null;
        sqlstr = sqlpart.toString();
        argpart = sqlarg.toArray();
        CaseObject pageObj = buildPagination(cnd, sqlstr, argpart);
        if (pageObj != null) {
            sqlstr = pageObj.getSqlpart();
            pagination = pageObj.getPagination();
        } else {
            pagination = new SimplePagination<>();
        }
        return new SQLObject(sqlstr, argpart, pagination);
    }

    @Override
    public long count(Cnd cnd) throws DbEx {
        if (cnd == null || cnd.getEntityClass() == null) {
            throw new DbEx("invalid cnd or entity");
        }
        String sqlstr = null;
        Object[] argpart = null;
        long start = System.currentTimeMillis();
        try {
            TableObject table = ReflectUtil.getTableValue(cnd.getEntityClass());
            CaseObject whereCase = buildWhereCase(cnd);
            StringBuffer sqlpart = new StringBuffer();
            sqlpart.append("select count(1) from ").append(table.getTableName());
            if (whereCase.getSqlpart().length() > 0) {
                sqlpart.append(" where").append(whereCase.getSqlpart().substring(0, whereCase.getSqlpart().length() - 3));
            }
            sqlstr = sqlpart.toString();
            argpart = whereCase.getArgpart().toArray();
            return template.queryForObject(sqlstr, argpart, Long.class);
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            logSqlHandle(start, sqlstr, argpart);
        }
    }

    @Override
    public long countComplex(Cnd cnd) throws DbEx {
        if (cnd == null || cnd.getEntityClass() == null) {
            throw new DbEx("invalid cnd or entity");
        }
        if (cnd.getEntityAlias() == null || cnd.getEntityAlias().length() == 0) {
            throw new DbEx("invalid entity alias");
        }
        String sqlstr = null;
        Object[] argpart = null;
        long start = System.currentTimeMillis();
        try {
            TableObject table = ReflectUtil.getTableValue(cnd.getEntityClass());
            CaseObject whereCase = buildWhereCase(cnd);
            StringBuffer sqlpart = new StringBuffer();
            argpart = whereCase.getArgpart().toArray();
            sqlpart.append("select count(1) from ").append(table.getTableName()).append(" ").append(cnd.getEntityAlias()).append(" ");
            CaseObject joinCase = buildJoinCase(cnd);
            if (joinCase.getSqlpart().length() > 0) {
                sqlpart.append(joinCase.getSqlpart());
            }
            if (whereCase.getSqlpart().length() > 0) {
                sqlpart.append("where").append(whereCase.getSqlpart().substring(0, whereCase.getSqlpart().length() - 3));
            }
            CaseObject groupbyCase = buildGroupbyCase(cnd);
            if (groupbyCase.getSqlpart().length() > 0) {
                sqlpart.append(groupbyCase.getSqlpart());
            }
            CaseObject sortbyCase = buildSortbyCase(cnd);
            if (sortbyCase.getSqlpart().length() > 0) {
                sqlpart.append(sortbyCase.getSqlpart());
            }
            sqlstr = sqlpart.toString();
            return template.queryForObject(sqlstr, argpart, Integer.class);
        } catch (Exception e) {
            throw new DbEx(e);
        } finally {
            logSqlHandle(start, sqlstr, argpart);
        }
    }

    private CaseObject buildWhereCase(Cnd cnd) throws DbEx {
        StringBuffer sqlpart = new StringBuffer();
        List<Object> argpart = new ArrayList<>();
        if (cnd.getConditions().size() == 0) {
            return new CaseObject(sqlpart.toString(), argpart);
        }
        for (Condition<?> condition : cnd.getConditions()) {
            OPT logic = condition.getLogic();
            if (OPT.EQ.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" =? and");
                argpart.add(condition.getValue());
            } else if (OPT.NOT_EQ.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" <>? and");
                argpart.add(condition.getValue());
            } else if (OPT.LT.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" <? and");
                argpart.add(condition.getValue());
            } else if (OPT.LTE.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" <=? and");
                argpart.add(condition.getValue());
            } else if (OPT.GT.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" >? and");
                argpart.add(condition.getValue());
            } else if (OPT.GTE.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" >=? and");
                argpart.add(condition.getValue());
            } else if (OPT.IS_NULL.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" is null and");
            } else if (OPT.IS_NOT_NULL.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" is not null and");
            } else if (OPT.BETWEEN.equals(logic)) {
                List btvalues = condition.getValues();
                if (btvalues == null || btvalues.size() != 2) {
                    throw new DbEx("between arg must 2");
                }
                sqlpart.append(" ").append(condition.getKey()).append(" between ? and ? and");
                argpart.addAll(btvalues);
            } else if (OPT.BETWEEN2.equals(logic)) {

            } else if (OPT.NOT_BETWEEN.equals(logic)) {
                List nbtvalues = condition.getValues();
                if (nbtvalues == null || nbtvalues.size() != 2) {
                    throw new DbEx("not between arg must 2");
                }
                sqlpart.append(" ").append(condition.getKey()).append(" not between ? and ? and");
                argpart.addAll(nbtvalues);
            } else if (OPT.IN.equals(logic)) {
                List invalues = condition.getValues();
                if (invalues == null || invalues.size() == 0) {
                    throw new DbEx("in arg must than 0");
                }
                StringBuffer inpart = new StringBuffer();
                for (Object value : invalues) {
                    inpart.append("?,");
                    argpart.add(value);
                }
                sqlpart.append(" ").append(condition.getKey()).append(" in(").append(inpart.substring(0, inpart.length() - 1)).append(") and");
            } else if (OPT.NOT_IN.equals(logic)) {
                List ninvalues = condition.getValues();
                if (ninvalues == null || ninvalues.size() == 0) {
                    throw new DbEx("not in arg must than 0");
                }
                StringBuffer notinpart = new StringBuffer();
                for (Object value : ninvalues) {
                    notinpart.append("?,");
                    argpart.add(value);
                }
                sqlpart.append(" ").append(condition.getKey()).append(" not in(").append(notinpart.substring(0, notinpart.length() - 1)).append(") and");
            } else if (OPT.LIKE.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" like concat('%',?,'%') and");
                argpart.add(condition.getValue());
            } else if (OPT.NOT_LIKE.equals(logic)) {
                sqlpart.append(" ").append(condition.getKey()).append(" not like concat('%',?,'%') and");
                argpart.add(condition.getValue());
            } else if (OPT.OR.equals(logic)) {
                List orvalues = condition.getValues();
                if (orvalues == null || orvalues.size() == 0) {
                    throw new DbEx("or arg must than 0");
                }
                StringBuffer orpart = new StringBuffer();
                for (Object value : orvalues) {
                    CaseObject obj = buildWhereCase((Cnd) value);
                    orpart.append(obj.getSqlpart().substring(0, obj.getSqlpart().length() - 3)).append(" or");
                    argpart.addAll(obj.getArgpart());
                }
                sqlpart.append(" (").append(orpart.substring(0, orpart.length() - 3)).append(") and");
            }
        }
        return new CaseObject(sqlpart.toString(), argpart);
    }

    private CaseObject buildGroupbyCase(Cnd cnd) throws DbEx {
        StringBuffer sqlpart = new StringBuffer();
        if (cnd.getGroupbys().size() == 0) {
            return new CaseObject(sqlpart.toString(), null);
        }
        for (String groupby : cnd.getGroupbys()) {
            if (groupby == null) {
                continue;
            }
            String key = groupby.trim();
            if (key.length() == 0) {
                continue;
            }
            sqlpart.append(" ").append(key).append(",");
        }
        if (sqlpart.length() == 0) {
            return new CaseObject(sqlpart.toString(), null);
        }
        return new CaseObject(new StringBuffer().append(" group by").append(sqlpart.substring(0, sqlpart.length() - 1)).toString(), null);
    }

    private CaseObject buildSortbyCase(Cnd cnd) throws DbEx {
        StringBuffer sqlpart = new StringBuffer();
        if (cnd.getOrderbys().size() == 0) {
            return new CaseObject(sqlpart.toString(), null);
        }
        for (OrderBy orderBy : cnd.getOrderbys()) {
            if (orderBy.getKey() == null) {
                continue;
            }
            String key = orderBy.getKey().trim();
            if (key.length() == 0) {
                continue;
            }
            sqlpart.append(" ").append(key).append(" ").append(orderBy.getValue().toString()).append(",");
        }
        if (sqlpart.length() == 0) {
            return new CaseObject(sqlpart.toString(), null);
        }
        return new CaseObject(new StringBuffer().append(" order by").append(sqlpart.substring(0, sqlpart.length() - 1)).toString(), null);
    }

    private CaseObject buildPagination(Cnd cnd, String sqlpart, Object[] argpart) throws DbEx {
        Pagination pagination = cnd.getPagination();
        if (pagination == null || pagination.getPageNo() == null || pagination.getPageSize() == null) {
            return null;
        }
        Dialect dialect = new MySQLDialect();
        if (pagination.isOffset()) {
            String limitSql = dialect.getOffsetSql(sqlpart, pagination.getPageNo(), pagination.getPageSize());
            return new CaseObject(limitSql, null, pagination);
        }
        String limitSql = dialect.getLimitSql(sqlpart, pagination.getPageNo(), pagination.getPageSize());
        String countSql = dialect.getCountSql(sqlpart);
        System.out.println("limitsql: " + limitSql);
        System.out.println("countSql: " + countSql);
        try {
            long pageTotal = template.queryForObject(countSql, argpart, Long.class);
            long pageNumber = 0;
            if (pageTotal % pagination.getPageSize() == 0) {
                pageNumber = pageTotal / pagination.getPageSize();
            } else {
                pageNumber = pageTotal / pagination.getPageSize() + 1;
            }
            pagination.setPageTotal(pageTotal);
            pagination.setPageNumber(pageNumber);
            return new CaseObject(limitSql, null, pagination);
        } catch (Exception e) {
            throw new DbEx(e);
        }
    }

    private CaseObject buildJoinCase(Cnd cnd) throws DbEx {
        StringBuffer sqlpart = new StringBuffer();
        List<Object> sqlarg = new ArrayList<>();
        if (cnd.getJoins().size() == 0) {
            return new CaseObject(sqlpart.toString(), sqlarg);
        }
        for (Join join : cnd.getJoins()) {
            String alias = join.getAlias() == null ? "" : join.getAlias().trim();
            String on = join.getJoin() == null ? "" : join.getJoin().trim();
            if (StringUtils.isEmpty(alias) || StringUtils.isEmpty(on)) {
                throw new DbEx("invalid join alias/on");
            }
            if (join.getEntity() instanceof Cnd) {
                SQLObject sqlObject = buildSQLObject((Cnd) join.getEntity());
                sqlpart.append("left join (").append(sqlObject.getSqlstr()).append(") ").append(alias).append(" on ").append(on).append(" ");
                sqlarg.add(Arrays.asList(sqlObject.getSqlarg()));
            } else {
                TableObject table = ReflectUtil.getTableValue(join.getEntity());
                if (MODE.LEFT.equals(join.getMode())) {
                    sqlpart.append("left join ").append(table.getTableName()).append(" ").append(alias).append(" on ").append(on).append(" ");
                } else if (MODE.RIGHT.equals(join.getMode())) {
                    sqlpart.append("right join ").append(table.getTableName()).append(" ").append(alias).append(" on ").append(on).append(" ");
                } else if (MODE.INNER.equals(join.getMode())) {
                    sqlpart.append("inner join ").append(table.getTableName()).append(" ").append(alias).append(" on ").append(on).append(" ");
                } else {
                    throw new DbEx("invalid join mode");
                }
            }
        }
        return new CaseObject(sqlpart.toString(), sqlarg);
    }

    private IDEntity[] listToArr(List entities) throws DbEx {
        if (entities == null || entities.size() == 0) {
            throw new DbEx("invalid entity len");
        }
        IDEntity[] arr = new IDEntity[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            Object data = entities.get(i);
            if (!(data instanceof IDEntity)) {
                throw new DbEx("invalid IDEntity class");
            }
            arr[i] = (IDEntity) data;
        }
        return arr;
    }

    private void logSqlHandle(long start, String sqlpart, Object[] argpart) {
        if (log.isDebugEnabled()) {
            log.warn(new StringBuffer(">>>>>> printSql >>>>>> ").append(sqlpart).toString());
        }
        if (start == 0) {
            return;
        }
        long qtime = dbConfig.getSlowQuery();
        if (qtime <= 0) {
            return;
        }
        long subtime = System.currentTimeMillis() - start;
        if (subtime < qtime) {
            return;
        }
        sqlpart = sqlpart == null ? "" : sqlpart;
        argpart = argpart == null ? new Object[]{} : argpart;
        sqlpart = sqlpart.replaceAll("\\?", "%s");
        log.warn(new StringBuffer(">>>>>> slowSql >>>>>> ").append(String.format(sqlpart, DataTypeUtils.objectToStringArr(argpart))).append(" >>>>>> cost >>>>>> ").append(subtime).append(" ms").toString());
    }

}
