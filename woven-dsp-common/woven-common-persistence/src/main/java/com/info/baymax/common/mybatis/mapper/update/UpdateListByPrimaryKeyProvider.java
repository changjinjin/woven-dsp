package com.info.baymax.common.mybatis.mapper.update;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;

import com.info.baymax.common.mybatis.mapper.MySqlHelper;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 批量修改操作处理器
 * @author jingwei.yang
 * @date 2019-05-28 15:06
 */
public class UpdateListByPrimaryKeyProvider extends MapperTemplate {

	private static final String DEF_ENTITY_NAME = "record";

	public UpdateListByPrimaryKeyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 说明： 根据主键批量修改，需要指定每条数据的主键值，并且修改所有字段. <br>
	 * @param ms
	 *            映射语句
	 * @return 动态sql
	 */
	public String updateListByPrimaryKey(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append(updateSetColumnsCaseWhenByList(entityClass, false));
		sql.append(MySqlHelper.wherePKColumnsByList(entityClass, DEF_ENTITY_NAME));
		return sql.toString();
	}

	/**
	 * 说明： 根据主键批量修改，需要指定每条数据的主键值，只修改非空字段，并且集合中的每个实体需要修改的字段是一样的. <br>
	 *
	 * @param ms
	 *            映射语句
	 * @return 动态sql
	 */
	public String updateListByPrimaryKeySelective(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder();
		sql.append(SqlHelper.updateTable(entityClass, tableName(entityClass)));
		sql.append(updateSetColumnsCaseWhenByList(entityClass, true));
		sql.append(MySqlHelper.wherePKColumnsByList(entityClass, DEF_ENTITY_NAME));
		return sql.toString();
	}

	/**
	 * 说明：拼接 case when条件下的set动态sql. <br>
	 *
	 * @param entityClass
	 *            实体类型
	 * @param notNull
	 *            是否判空
	 * @return 动态set sql
	 */
	private String updateSetColumnsCaseWhenByList(final Class<?> entityClass, boolean notNull) {
		StringBuilder sql = new StringBuilder();
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		sql.append("<set>");
		for (EntityColumn column : columnList) {
			if (!column.isId() && column.isUpdatable()) {
				sql.append("<trim prefix=\"").append(column.getColumn()).append(" = case\" suffix=\"end,\" suffixOverrides=\",\">");
				sql.append("<foreach collection=\"list\" item=\"").append(DEF_ENTITY_NAME).append("\" index=\"index\">");
				if (notNull) {
					sql.append("<if test=\"").append(DEF_ENTITY_NAME).append(".").append(column.getProperty()).append(" != null\">");
				}
				sql.append(" when ").append(MySqlHelper.pKColumnsCondition(entityClass, DEF_ENTITY_NAME)).append(" then ").append(column.getColumnHolder(DEF_ENTITY_NAME));
				if (notNull) {
					sql.append("</if>");
				}
				sql.append("</foreach>");
				sql.append("</trim>");
			}
		}
		sql.append("</set>");
		return sql.toString();
	}

}
