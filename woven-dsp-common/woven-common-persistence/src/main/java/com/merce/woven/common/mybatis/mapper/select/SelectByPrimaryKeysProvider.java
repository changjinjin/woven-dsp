package com.merce.woven.common.mybatis.mapper.select;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

import java.util.Set;

/**
 * 根据主键集合查询
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:05
 */
public class SelectByPrimaryKeysProvider extends MapperTemplate {

    public SelectByPrimaryKeysProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 根据主键集合进行查询，类中只有存在一个带有@Id注解的字段，如果对应表存在联合主键或者没有主键则生成空实现，即sql为空. <br>
     *
     * @param ms 映射语句
     * @return 动态sql
     */
    public String selectByPrimaryKeys(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        // 将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        Set<EntityColumn> columnList = EntityHelper.getPKColumns(entityClass);
        if (columnList.size() == 1) {
            EntityColumn column = columnList.iterator().next();
            sql.append("<where>");
            sql.append(column.getColumn());
            sql.append(" in ");
            sql.append("<foreach collection=\"list\" item=\"").append(column.getProperty()).append("\" open=\"(\" separator=\",\" close=\")\" index=\"index\" >");
            sql.append(column.getColumnHolder(null, null, null));
            sql.append("</foreach>");
            sql.append("</where>");
        } else {
            throw new MapperException("继承 selectByPrimaryKeys 方法的实体类[" +
                    entityClass.getCanonicalName() + "]中必须有且只有一个带有 @Id注解的字段");
        }
        return sql.toString();
    }

}
