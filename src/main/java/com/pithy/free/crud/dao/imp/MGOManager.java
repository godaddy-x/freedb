package com.pithy.free.crud.dao.imp;

import com.pithy.free.crud.dao.IDBase;
import com.pithy.free.crud.domain.IDEntity;
import com.pithy.free.crud.ex.DbEx;
import com.pithy.free.pageable.Pagination;
import com.pithy.free.sqlcode.condition.Cnd;

import java.util.List;

/**
 * mongo数据库 - CRUD封装实现类
 *
 * @author shadow
 */
public class MGOManager implements IDBase {

    @Override
    public int save(IDEntity... entities) throws DbEx {
        return 0;
    }

    @Override
    public int save(List entities) throws DbEx {
        return 0;
    }

    @Override
    public int update(IDEntity... entities) throws DbEx {
        return 0;
    }

    @Override
    public int update(List entities) throws DbEx {
        return 0;
    }

    @Override
    public int update(Cnd cnd) throws DbEx {
        return 0;
    }

    @Override
    public int delete(IDEntity... entities) throws DbEx {
        return 0;
    }

    @Override
    public int delete(List entities) throws DbEx {
        return 0;
    }

    @Override
    public int delete(Cnd cnd) throws DbEx {
        return 0;
    }

    @Override
    public <E> E findByPK(Object pkval, Class<E> mapper) throws DbEx {
        return null;
    }

    @Override
    public <E> E findOne(Cnd cnd, Class<E> mapper) throws DbEx {
        return null;
    }

    @Override
    public <E> List<E> findList(Cnd cnd, Class<E> mapper) throws DbEx {
        return null;
    }

    @Override
    public <E> Pagination<E> findPage(Cnd cnd, Class<E> mapper) throws DbEx {
        return null;
    }

    @Override
    public <E> E findOneComplex(Cnd cnd, Class<E> mapper) throws DbEx {
        return null;
    }

    @Override
    public <E> List<E> findListComplex(Cnd cnd, Class<E> mapper) throws DbEx {
        return null;
    }

    @Override
    public <E> Pagination<E> findPageComplex(Cnd cnd, Class<E> mapper) throws DbEx {
        return null;
    }

    @Override
    public long count(Cnd cnd) throws DbEx {
        return 0;
    }

    @Override
    public long countComplex(Cnd cnd) throws DbEx {
        return 0;
    }

}
