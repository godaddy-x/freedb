package com.pithy.free;

import com.pithy.free.crud.dao.IDBase;
import com.pithy.free.crud.dao.imp.RDBManager;
import com.pithy.free.crud.domain.DBConfig;
import com.pithy.free.crud.ex.DbEx;
import com.pithy.free.scan.TableClassLoader;
import com.pithy.free.sqlcode.aconst.SortBy;
import com.pithy.free.sqlcode.condition.Cnd;
import com.pithy.free.sqlcode.condition.imp.SQL;
import com.pithy.free.utils.ReflectUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class MainTest {

    private static JdbcTemplate initJdbcTemplate() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/pgwjc?useUnicode=true&characterEncoding=utf8&allowMultiQueries=true&serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return new JdbcTemplate(dataSource);
    }

    public static void main(String[] args) throws DbEx {

        // 扫描实体对象
        TableClassLoader.registry("com.pithy.free");

        // 初始化DB操作对象
        IDBase manager = new RDBManager(initJdbcTemplate(), new DBConfig(100));

        // 实体示例
        Student student = new Student();
        student.setId(1l);
        student.setName1("张三1111");
        student.setSex(2);
        student.setAdmin(true);
        student.setUpdateAt(new Date());
        student.setCreateAt(new Date());
        Student student1 = new Student();
        student1.setId(7l);
        student1.setName1("李四3");
        student1.setSex(3);
        student1.setCreateAt(new Date());

        // 保存实体
        manager.save(student, student1);
        // 更新实体
        manager.update(student, student1);
        // 根据条件更新指定字段
        manager.update(new SQL(Student.class)
                .eq("id", 1)
                .upset(new String[]{"id", "name", "createAt"}, 1, "王武111", new Date()));
        // 删除实体
        manager.delete(student, student1);
        // 根据条件删除
        manager.delete(new SQL(Student.class)
                .in("id", 1291716720588791809l, 2, 12, 13));
        // 根据条件统计
        manager.count(new SQL(Student.class).eq("id", 1));
        // 根据主键获取实体
        manager.findByPK(3, Student.class);
        // 分页查询列表
        manager.findPage(new SQL(Student.class));
        // 根据条件查询列表
        manager.findList(new SQL(Student.class).fields("name as name1")
                        .or(new SQL().eq("id", 3), new SQL().in("id", 4, 21))
                        .groupby("id").orderby("id", SortBy.A)
                , String.class);
        // 根据条件自定义统计
        manager.countComplex(new SQL(Student.class, "a")
                .fields("count(a.name) id")
                .eq("a.id", 1291716720588791810l).between("createAt", LocalDate.now(), LocalDate.now()));
        // 根据条件进行左连接统计
        manager.countComplex(new SQL(Student.class, "a").leftJoin(Teacher.class, "b", "a.id=b.studentId").fields("a.name as name1"));
        // 根据条件进行左连接查询
        manager.findListComplex(new SQL(Student.class, "a").leftJoin(Teacher.class, "b", "a.id=b.studentId").fields("a.name as name1"));
        // 根据条件进行左连接分页查询(包含左连接子查询)
        Cnd sql = new SQL(Student.class, "a")
                .leftJoin(Teacher.class, "b", "a.id=b.studentId")
                .leftJoin(new SQL(Student.class, "a")
                        .leftJoin(Teacher.class, "b", "a.id=b.id").fields("a.id").eq("a.id", 3), "c", "a.id=c.id")
                .fields("a.name as name1").eq("a.id", 1);
        manager.findPageComplex(sql, Student.class);
    }
}
