package com.pithy.free;

import com.pithy.free.crud.dao.IDBase;
import com.pithy.free.crud.dao.imp.RDBManager;
import com.pithy.free.crud.domain.DBConfig;
import com.pithy.free.crud.ex.DbEx;
import com.pithy.free.scan.TableClassLoader;
import com.pithy.free.sqlcode.aconst.SortBy;
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

    public static void main(String[] args) {
        TableClassLoader.registry("com.pithy.free");
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
        IDBase manager = new RDBManager(initJdbcTemplate(), new DBConfig(100));
        try {
            manager.save(student, student1);
            manager.update(student, student1);
            manager.update(new SQL(Student.class)
                    .eq("id", 1)
                    .upset(new String[]{"id", "name", "createAt"}, 1, "王武111", new Date()));
            manager.delete(student, student1);
            manager.delete(new SQL(Student.class)
                    .in("id", 1291716720588791809l, 2, 12, 13));
            manager.count(new SQL(Student.class).eq("id", 1));
            Student s = manager.findByPK(3, Student.class);
            manager.findPage(new SQL(Student.class));
            manager.findList(new SQL(Student.class).fields("name as name1")
                            .or(new SQL().eq("id", 3), new SQL().in("id", 4, 21))
                            .groupby("id").orderby("id", SortBy.A)
                    , String.class);
            manager.countComplex(new SQL(Student.class, "a")
                    .fields("count(a.name) id")
                    .eq("a.id", 1291716720588791810l).between("createAt", LocalDate.now(), LocalDate.now()));
            manager.countComplex(new SQL(Student.class, "a").leftJoin(Teacher.class, "b", "a.id=b.studentId").fields("a.name as name1"));
            manager.findListComplex(new SQL(Student.class, "a").leftJoin(Teacher.class, "b", "a.id=b.studentId").fields("a.name as name1"));
            System.out.println("----");
        } catch (DbEx dbEx) {
            dbEx.printStackTrace();
        }
    }
}
