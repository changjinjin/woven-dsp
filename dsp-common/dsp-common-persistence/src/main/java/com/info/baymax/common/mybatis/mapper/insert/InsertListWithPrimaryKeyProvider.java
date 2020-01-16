package com.info.baymax.common.mybatis.mapper.insert;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 批量插入操作处理器
 * @author jingwei.yang
 * @date 2019-05-28 15:04
 */
public class InsertListWithPrimaryKeyProvider extends MapperTemplate {

	public InsertListWithPrimaryKeyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 说明： 批量插入，支持批量插入的数据库可以使用，例如MySQL,H2等，该接口限制实体包含主键属性并且插入时指定主键值. <br>
	 *
	 * @param ms
	 *            映射语句
	 * @return 动态sql
	 */
	public String insertListWithPrimaryKey(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.insertIntoTable(entityClass, tableName(entityClass)));
		// 第二个参数 skipId设置为false
		sql.append(SqlHelper.insertColumns(entityClass, false, false, false));
		sql.append(" VALUES ");
		sql.append("<foreach collection=\"list\" item=\"record\" separator=\",\" >");
		sql.append("<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (EntityColumn column : columnList) {
			if (column.isInsertable()) {
				sql.append(column.getColumnHolder("record") + ",");
			}
		}
		sql.append("</trim>");
		sql.append("</foreach>");
		return sql.toString();
	}
}
