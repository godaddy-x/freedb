package com.pithy.free.sqlcode.domain;

import com.pithy.free.crud.aconst.MODE;
import com.pithy.free.sqlcode.aconst.OPT;

import java.util.ArrayList;
import java.util.List;

public class Join {

    private MODE mode;
    private Object entity;
    private String alias;
    private String join;

    public Join() {

    }

    public Join(MODE mode, Object entity, String alias, String join) {
        this.mode = mode;
        this.entity = entity;
        this.alias = alias;
        this.join = join;
    }

    public MODE getMode() {
        return mode;
    }

    public void setMode(MODE mode) {
        this.mode = mode;
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Object entity) {
        this.entity = entity;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getJoin() {
        return join;
    }

    public void setJoin(String join) {
        this.join = join;
    }

}
