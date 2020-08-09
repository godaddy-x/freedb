package com.pithy.free.crud.dao;

import com.pithy.free.crud.ex.DbEx;
import com.pithy.free.crud.domain.IDEntity;
import com.pithy.free.pageable.Pagination;
import com.pithy.free.sqlcode.condition.Cnd;

import java.util.List;

/**
 * @author shadow
 */
public interface IDBase {

    // 保存数据
    public int save(IDEntity... entities) throws DbEx;

    // 保存数据
    public int save(List entities) throws DbEx;

    // 修改数据
    public int update(IDEntity... entities) throws DbEx;

    // 修改数据
    public int update(List entities) throws DbEx;

    // 按条件修改数据
    public int update(Cnd cnd) throws DbEx;

    // 通过对象删除数据
    public int delete(IDEntity... entities) throws DbEx;

    // 通过对象删除数据
    public int delete(List entities) throws DbEx;

    // 通过条件删除数据
    public int delete(Cnd cnd) throws DbEx;

    // 通过ID查询数据
    public <E> E findByPK(Object pkval, Class<E> mapper) throws DbEx;

    // 查询数据
    public <E> E findOne(Cnd cnd, Class<E> mapper) throws DbEx;

    // 查询列表数据
    public <E> List<E> findList(Cnd cnd, Class<E> mapper) throws DbEx;

    // 分页查询列表数据
    public <E> Pagination<E> findPage(Cnd cnd, Class<E> mapper) throws DbEx;

    // 复杂查询数据
    public <E> E findOneComplex(Cnd cnd, Class<E> mapper) throws DbEx;

    // 复杂查询列表数据
    public <E> List<E> findListComplex(Cnd cnd, Class<E> mapper) throws DbEx;

    // 复杂分页查询列表数据
    public <E> Pagination<E> findPageComplex(Cnd cnd, Class<E> mapper) throws DbEx;

    // 通过条件count查询
    public long count(Cnd cnd) throws DbEx;

    // 通过条件count复杂查询
    public long countComplex(Cnd cnd) throws DbEx;

}
