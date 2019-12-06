package com.merce.woven.common.mybatis.mapper;

import tk.mybatis.mapper.LogicDeleteException;
import tk.mybatis.mapper.MapperException;
import tk.mybatis.mapper.annotation.LogicDelete;
import tk.mybatis.mapper.annotation.Version;
import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.StringUtil;
import tk.mybatis.mapper.version.VersionException;

import java.util.Set;

/**
 * SqlHelper扩展
 *
 * @author jingwei.yang
 * @date 2019-05-28 15:07
 */
public class MySqlHelper extends SqlHelper {

	public static String replaceIntoTable(Class<?> entityClass, String defaultTableName) {
		StringBuilder sql = new StringBuilder();
		sql.append("REPLACE INTO ");
		sql.append(getDynamicTableName(entityClass, defaultTableName));
		sql.append(" ");
		return sql.toString();
	}

	public static String columnsIfNotNullCondition(Class<?> entityClass, String entityName, boolean empty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\"(\" suffix=\")\" prefixOverrides=\"AND\">");
		// 获取全部列
		Set<EntityColumn> columnList = EntityHelper.getColumns(entityClass);
		for (EntityColumn column : columnList) {
			sql.append(getIfNotNull(entityName, column, " AND " + column.getColumnEqualsHolder(entityName), empty));
		}
		sql.append("</trim>");
		return sql.toString();
	}

	public static String pKColumnsCondition(Class<?> entityClass, String entityName) {
		StringBuilder sql = new StringBuilder();
		sql.append("<trim prefix=\"(\" suffix=\")\" prefixOverrides=\"AND\">");
		// 获取全部主键列
		Set<EntityColumn> pKcolumnList = EntityHelper.getPKColumns(entityClass);
		if (pKcolumnList == null || pKcolumnList.size() < 1) {
			throw new MapperException("该方法的实体类[" + entityClass.getCanonicalName() + "]中必须只有一个带有 @Id 注解的字段");
		}
		for (EntityColumn column : pKcolumnList) {
			sql.append(" AND " + column.getColumnEqualsHolder(entityName));
		}
		sql.append("</trim>");
		return sql.toString();
	}

	public static String wherePKColumnsByList(Class<?> entityClass, String entityName) {
		StringBuilder sql = new StringBuilder();
		sql.append("<where>");
		sql.append("<foreach collection=\"list\" separator=\" or\" item=\"").append(entityName)
				.append("\" index=\"index\">");
		sql.append(pKColumnsCondition(entityClass, entityName));
		sql.append("</foreach>");
		sql.append("</where>");
		return sql.toString();
	}

	public static String whereAllColumnsByList(Class<?> entityClass, String entityName, boolean empty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<where>");
		sql.append("<foreach collection=\"list\" separator=\" or\" item=\"").append(entityName)
				.append("\" index=\"index\">");
		sql.append(columnsIfNotNullCondition(entityClass, entityName, empty));
		sql.append("</foreach>");
		sql.append("</where>");
		return sql.toString();
	}

	/**
	 * update set列
	 *
	 * @param entityClass
	 * @param entityName
	 *            实体映射名
	 * @param notNull
	 *            是否判断!=null
	 * @param notEmpty
	 *            是否判断String类型!=''
	 * @return
	 */
	public static Object updateSetColumnsWithForceVersion(Class<?> entityClass, String entityName, boolean notNull,
			boolean notEmpty) {
		StringBuilder sql = new StringBuilder();
		sql.append("<set>");
		// 获取全部列
		Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
		// 对乐观锁的支持
		EntityColumn versionColumn = null;
		// 逻辑删除列
		EntityColumn logicDeleteColumn = null;
		// 当某个列有主键策略时，不需要考虑他的属性是否为空，因为如果为空，一定会根据主键策略给他生成一个值
		for (EntityColumn column : columnSet) {
			if (column.getEntityField().isAnnotationPresent(Version.class)) {
				if (versionColumn != null) {
					throw new VersionException(
							entityClass.getCanonicalName() + " 中包含多个带有 @Version 注解的字段，一个类中只能存在一个带有 @Version 注解的字段!");
				}
				versionColumn = column;
			}
			if (column.getEntityField().isAnnotationPresent(LogicDelete.class)) {
				if (logicDeleteColumn != null) {
					throw new LogicDeleteException(entityClass.getCanonicalName()
							+ " 中包含多个带有 @LogicDelete 注解的字段，一个类中只能存在一个带有 @LogicDelete 注解的字段!");
				}
				logicDeleteColumn = column;
			}
			if (!column.isId() && column.isUpdatable()) {
				if (column == versionColumn) {
					sql.append("<if test=\"forceVersion\">");
					Version version = versionColumn.getEntityField().getAnnotation(Version.class);
					String versionClass = version.nextVersion().getCanonicalName();
					sql.append("<bind name=\"").append(column.getProperty()).append("Version\" value=\"");
					sql.append("@tk.mybatis.mapper.version.VersionUtil@nextVersion(").append("@").append(versionClass)
							.append("@class, ");
					if (StringUtil.isNotEmpty(entityName)) {
						sql.append(entityName).append(".");
					}
					sql.append(column.getProperty()).append(")\"/>");
					sql.append(column.getColumn()).append(" = #{").append(column.getProperty()).append("Version},");
					sql.append("</if>");
				} else if (column == logicDeleteColumn) {
					sql.append(logicDeleteColumnEqualsValue(column, false)).append(",");
				} else if (notNull) {
					sql.append(SqlHelper.getIfNotNull(entityName, column,
							column.getColumnEqualsHolder(entityName) + ",", notEmpty));
				} else {
					sql.append(column.getColumnEqualsHolder(entityName) + ",");
				}
			}
		}
		sql.append("</set>");
		return sql.toString();
	}

	public static String wherePKColumnsUseVersion(Class<?> entityClass, String entityName, boolean useVersion) {
		StringBuilder sql = new StringBuilder();
		boolean hasLogicDelete = hasLogicDeleteColumn(entityClass);
		sql.append("<where>");
		Set<EntityColumn> columnSet = EntityHelper.getPKColumns(entityClass);
		for (EntityColumn column : columnSet) {
			sql.append(" AND ").append(column.getColumnEqualsHolder(entityName));
		}
		if (useVersion) {
			sql.append("<if test=\"forceVersion\">");
			sql.append(whereVersion(entityClass,entityName));
			sql.append("</if>");
		}

		if (hasLogicDelete) {
			sql.append(whereLogicDelete(entityClass, false));
		}

		sql.append("</where>");
		return sql.toString();
	}



	private static Object whereVersion(Class<?> entityClass,String entityName) {
		Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
		boolean hasVersion = false;
		String result = "";
		for (EntityColumn column : columnSet) {
			if (column.getEntityField().isAnnotationPresent(Version.class)) {
				if (hasVersion) {
					throw new VersionException(entityClass.getCanonicalName()
							+ " 中包含多个带有 @Version 注解的字段，一个类中只能存在一个带有 @Version 注解的字段!");
				}
				hasVersion = true;
				result = " AND " + column.getColumnEqualsHolder(entityName);
			}
		}
		return result;
	}
}
