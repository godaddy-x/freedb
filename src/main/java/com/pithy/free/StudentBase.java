package com.pithy.free;

import com.pithy.free.anno.Column;
import com.pithy.free.anno.Table;
import com.pithy.free.crud.domain.IDEntity;

import java.util.Date;

public class StudentBase implements IDEntity {

    @Column(ignore = true)
    private Date updateAt;

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
