package com.merce.woven.common.mybatis.mapper.insert;

import java.util.Set;

import org.apache.ibatis.mapping.MappedStatement;

import com.merce.woven.common.mybatis.mapper.MySqlHelper;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 批量覆盖式插入操作处理器
 * @author jingwei.yang
 * @date 2019-05-28 15:05
 */
public class ReplaceListWithPrimaryKeyProvider extends MapperTemplate {

	public ReplaceListWithPrimaryKeyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	/**
	 * 说明：批量插入,如果表中存在主键，这里可以指定主键值，不同于主键自增长的模式，主键的名称“id”. <br>
	 * @param ms
	 *            映射语句
	 * @return 动态sql
	 */
	public String replaceListWithPrimaryKey(MappedStatement ms) {
		final Class<?> entityClass = getEntityClass(ms);
		// 开始拼sql
		StringBuilder sql = new StringBuilder();
		sql.append(MySqlHelper.replaceIntoTable(entityClass, tableName(entityClass)));
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
