# freedb
free and concise database operation

# 条件接口

public interface Cnd {

    // =
        public Cnd eq(String key, Object value);
    
        // <>
        public Cnd noteq(String key, Object value);
    
        // <
        public Cnd lt(String key, Object value);
    
        // <=
        public Cnd lte(String key, Object value);
    
        // >
        public Cnd gt(String key, Object value);
    
        // >=
        public Cnd gte(String key, Object value);
    
        // is null
        public Cnd isnull(String key);
    
        // is not null
        public Cnd notnull(String key);
    
        // between x and y
        public Cnd between(String key, Object value1, Object value2);
    
        // between time, >= a b <
        public Cnd betweenDate(String key, Object value1, Object value2);
    
        // not between x and y
        public Cnd notbetween(String key, Object value1, Object value2);
    
        // in
        public Cnd in(String key, Object... values);
    
        // in
        public Cnd in(String key, Object values);
    
        // not in
        public Cnd notin(String key, Object... values);
    
        // not in
        public Cnd notin(String key, Object values);
    
        // like
        public Cnd like(String key, Object value);
    
        // not like
        public Cnd notlike(String key, Object value);
    
        // or
        public Cnd or(Cnd... cnds);
    
        // 分页方法(根据数据库方言)
        public Cnd limit(Long pageSize);
    
        // 分页方法(根据数据库方言)
        public Cnd limit(Long pageNo, Long pageSize);
    
        // 分页方法(根据数据库方言)
        public Cnd limit(Long pageNo, Long pageSize, boolean spilled);
    
        // 分页方法(根据数据库方言)
        public Cnd offset(Long offset, Long limit);
    
        // group by
        public Cnd groupby(String... keys);
    
        // order by
        public Cnd orderby(String key, SortBy sortBy);
    
        // order by
        public Cnd orderby(OrderBy... orderBy);
    
        // 获取指定字段
        public Cnd fields(String... keys);
    
        // 指定更新字段
        public Cnd upset(String[] keys, Object... values);
    
        // 左连接表
        public Cnd leftJoin(Class entity, String alias, String join);
    
        // 左连接表
        public Cnd leftJoin(Cnd entity, String alias, String join);
    
        // 右连接表
        public Cnd rightJoin(Class entity, String alias, String join);
    
        // 右连接表
        public Cnd rightJoin(Cnd entity, String alias, String join);
    
        // 内连接表
        public Cnd innerJoin(Class entity, String alias, String join);
    
        // 内连接表
        public Cnd innerJoin(Cnd entity, String alias, String join);
    
        // 复制条件
        public Cnd copy(Cnd cnd);

}

# 操作示例

public class StudentBase implements IDEntity {

    @Column(ignore = true)
    private Date updateAt;
    
}

@Table(name = "student", pk = "id")

public class Student extends StudentBase {

    private Long id;
    @Column(name = "name")
    private String name1;
    private Integer sex;
    @Column(ignore = true)
    private boolean admin;
    private Date createAt;

}

@Table(name = "teacher", pk = "id")

public class Teacher implements IDEntity {

    private Long id;
    private Long studentId;
    private String name;
    private Integer sex;
    @Column(ignore = true)
    private boolean admin;
    private Date createAt;

}

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
        // 根据条件进行左连接分页查询
        Cnd sql = new SQL(Student.class, "a")
                        .leftJoin(Teacher.class, "b", "a.id=b.studentId")
                        .leftJoin(new SQL(Student.class, "a")
                                .leftJoin(Teacher.class, "b", "a.id=b.id").fields("a.id").eq("a.id", 3), "c", "a.id=c.id")
                        .fields("a.name as name1").eq("a.id", 1);
        manager.findPageComplex(sql, Student.class);
    }
}