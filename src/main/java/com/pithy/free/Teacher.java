package com.pithy.free;

import com.pithy.free.anno.Column;
import com.pithy.free.anno.Table;
import com.pithy.free.crud.domain.IDEntity;

import java.util.Date;

@Table(name = "teacher", pk = "id")
public class Teacher implements IDEntity {

    private Long id;
    private Long studentId;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
}
