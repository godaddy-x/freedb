package com.pithy.free.scan;

import com.pithy.free.anno.Column;
import com.pithy.free.anno.Table;
import com.pithy.free.crud.domain.ColumnObject;
import com.pithy.free.crud.domain.FieldObject;
import com.pithy.free.crud.domain.TableObject;
import com.pithy.free.crud.ex.DbEx;
import com.pithy.free.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class TableClassLoader {

    private final static Logger log = LoggerFactory.getLogger(TableClassLoader.class);
    private final static Map<String, TableObject> tableObjectMap = new HashMap<>();

    public static TableObject getTableObject(Object object) throws DbEx {
        if (object == null) {
            throw new DbEx("invalid object null");
        }
        Class clz = object.getClass();
        if (clz == Class.class) {
            clz = (Class) object;
        } else {
            clz = object.getClass();
        }
        TableObject tableObject = tableObjectMap.get(clz.getName());
        if (tableObject == null) {
            throw new DbEx("invalid table class null");
        }
        return tableObject;
    }

    public static void registry(String basePackages) {
        Set<Class> classes = ClassScaner.scan(basePackages, Table.class);
        if (classes == null || classes.size() == 0) {
            log.warn(basePackages + ": entity not found");
            return;
        }
        try {
            for (Class clz : classes) {
                Object obj = clz.newInstance();
                Table ann = obj.getClass().getAnnotation(Table.class);
                String tableName = ann.name();
                if (tableName == null || tableName.length() == 0) {
                    throw new Exception("invalid table name");
                }
                String pkName = ann.pk();
                if (pkName == null || pkName.length() == 0) {
                    throw new Exception("invalid table pk name");
                }
                // 实体/数据库对应参数
                TableObject tableObject = new TableObject();
                tableObject.setTableName(tableName);
                tableObject.setPkName(pkName);
                tableObject.setModelClass(obj.getClass());
                tableObject.setModelClassName(obj.getClass().getName());
                // 实体字段列表
                List<FieldObject> fields = ReflectUtil.getFieldObjects(obj);
                if (fields == null || fields.size() == 0) {
                    continue;
                }
                List<ColumnObject> columnObjects = new ArrayList<>(fields.size());
                for (FieldObject fieldObject : fields) {
                    Field field = fieldObject.getField();
                    field.setAccessible(true);
                    ColumnObject columnObject = new ColumnObject();
                    Column column = field.getAnnotation(Column.class);
                    if (column != null && column.ignore()) {
                        continue;
                    }
                    columnObject.setClassType(fieldObject.getClazz());
                    columnObject.setMdName(field.getName());
                    columnObject.setDbName(column == null || column.name().length() == 0 ? field.getName() : column.name());
                    columnObject.setIgnore(column == null ? false : column.ignore());
                    columnObject.setFieldType(field.getType());
                    if (pkName.equals(columnObject.getDbName())) {
                        columnObject.setPK(true);
                        tableObject.setPkColumn(columnObject);
                    }
                    columnObjects.add(columnObject);
                }
                tableObject.setColumnObjects(columnObjects);
                tableObjectMap.put(tableObject.getModelClassName(), tableObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        TableClassLoader.registry("com.pithy.free");
    }

}
