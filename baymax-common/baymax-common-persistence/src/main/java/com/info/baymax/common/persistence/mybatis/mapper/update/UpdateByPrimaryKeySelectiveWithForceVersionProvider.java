package com.info.baymax.common.persistence.mybatis.mapper.update;

import com.info.baymax.common.persistence.mybatis.mapper.MySqlHelper;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

@RegisterMapper
public class UpdateByPrimaryKeySelectiveWithForceVersionProvider extends MapperTemplate {

    public UpdateByPrimaryKeySelectiveWithForceVersionProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String updateByPrimaryKeySelectiveWithForceVersion(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
        String entityName = "record";
        sql.append(MySqlHelper.updateSetColumnsWithForceVersion(entityClass, entityName, true, isNotEmpty()));
        sql.append(MySqlHelper.wherePKColumnsUseVersion(entityClass, entityName, true));
        return sql.toString();
    }

}
