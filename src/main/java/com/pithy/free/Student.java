package com.pithy.free;

import com.pithy.free.anno.Column;
import com.pithy.free.anno.Table;
import com.pithy.free.crud.domain.IDEntity;

import java.util.Date;

@Table(name = "student", pk = "id")
public class Student implements IDEntity {

    private Long id;
    @Column(name = "name")
    private String name1;
    private Integer sex;
    @Column(ignore = true)
    private boolean admin;
    private Date createAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
