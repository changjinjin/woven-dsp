package com.info.baymax.common.mybatis.mapper.delete;

import org.apache.ibatis.mapping.MappedStatement;

import com.info.baymax.common.mybatis.mapper.MySqlHelper;

import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 批量删除
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:03
 */
public class DeleteListByPrimaryKeyProvider extends MapperTemplate {

	public DeleteListByPrimaryKeyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 通过实体字段为条件批量删除，根据参数中每个实体的字段值进行匹配删除，相当于视图属性值建立联合条件匹配删除. <br>
	 *
	 * @param ms
	 *            映射语句
	 * @return 批量删除动态sql
	 */
	public String deleteList(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
		sql.append(MySqlHelper.whereAllColumnsByList(entityClass, "record", true));
		return sql.toString();
	}

	/**
	 * 通过主键批量删除，参数集合中每个实体的主键字段不为空，根据对应主键值匹配删除. <br>
	 *
	 * @param ms
	 *            映射语句
	 * @return 根据主键批量删除动态sql
	 */
	public String deleteListByPrimaryKey(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
		sql.append(MySqlHelper.wherePKColumnsByList(entityClass, "record"));
		return sql.toString();
	}
}
