package com.merce.woven.common.mybatis.mapper.example;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.merce.woven.common.mybatis.mapper.example.Example.Criteria;
import com.merce.woven.common.mybatis.mapper.example.Example.CriteriaItem;
import com.merce.woven.common.mybatis.mapper.example.Example.Criterion;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.mapperhelper.EntityHelper;

/**
 * 通用的Example查询SQL辅助器
 *
 * @author jingwei.yang
 * @date 2019年7月17日 下午9:24:02
 */
public class ExampleSqlHelper {

    public static String where(Criteria criteria) {
        if (criteria == null) {
            return "";
        }
        return trimAndOr(condition(criteria));
    }

    public static String condition(Criteria criteria) {
        StringBuffer buff = new StringBuffer();
        List<CriteriaItem> ordItems = criteria.getOrdItems();
        if (ordItems != null) {
            buff.append(criteria.getAndOr()).append(" ").append("(");
            buff.append(trimAndOr(condition(ordItems)));
            buff.append(")").append(" ");
        }
        return buff.toString();
    }

    private static String condition(List<CriteriaItem> ordItems) {
        StringBuffer buff = new StringBuffer();
        for (CriteriaItem item : ordItems) {
            if (item instanceof Criterion) {
                buff.append(condition((Criterion) item));
            } else {
                buff.append(condition((Criteria) item));
            }
        }
        return buff.toString();
    }

    public static String condition(Criterion criterion) {
        StringBuffer buff = new StringBuffer();
        if (criterion != null) {
            buff.append(criterion.getAndOr()).append(" ");

            // 没有值
            if (criterion.isNoValue()) {
                buff.append(criterion.getCondition()).append(" ");
            }

            // 一个值
            if (criterion.isSingleValue()) {
                buff.append(criterion.getCondition()).append(" ").append(concatValue(criterion.getValue())).append("");
            }

            // 两个值
            if (criterion.isBetweenValue()) {
                buff.append(criterion.getCondition()).append(" ").append(concatValue(criterion.getValue())).append(" ")
                    .append("and").append(" ").append(concatValue(criterion.getValue())).append("");
            }

            // 多个值
            if (criterion.isListValue()) {
                buff.append(criterion.getCondition()).append(" ").append("(")
                    .append(StringUtils.join(concatValue(criterion.getValue()), ",")).append(")");
            }
            buff.append(" ");
        }
        return buff.toString();
    }

    public static Object concatValue(Object value) {
        if (value instanceof String) {
            value = "'" + (String) value + "'";
        }
        return value;
    }

    public static String trimAndOr(String condition) {
        if (StringUtils.isNotEmpty(condition) && StringUtils.isNotEmpty(condition.trim())) {
            condition = condition.trim();
            if (condition.startsWith("and") || condition.startsWith("AND")) {
                condition = condition.replaceFirst("and|AND", "");
            }
            if (condition.startsWith("or") || condition.startsWith("OR")) {
                condition = condition.replaceFirst("or|OR", "");
            }
        }
        return condition;
    }

    /**
     * 获取表名 - 支持动态表名
     *
     * @param entityClass
     * @param tableName
     * @return
     */
    public static String getExampleDynamicTableName(Class<?> entityClass, String tableName) {
        // if (IDynamicTableName.class.isAssignableFrom(entityClass)) {
        StringBuilder sql = new StringBuilder();
        sql.append("<choose>");
        sql.append(
            "<when test=\"@tk.mybatis.mapper.util.OGNL@isDynamicParameter(_parameter) and dynamicTableName != null and dynamicTableName != ''\">");
        sql.append("${dynamicTableName}\n");
        sql.append("</when>");
        // 不支持指定列的时候查询全部列
        sql.append("<otherwise>");
        sql.append(tableName).append(exampleTableAlias(null, false));
        sql.append("</otherwise>");
        sql.append("</choose>");
        return sql.toString();
        /*
         * } else { return tableName; }
         */
    }

    public static String exampleFromTable(Class<?> entityClass, String defaultTableName) {
        StringBuilder sql = new StringBuilder();
        sql.append(" FROM ");
        sql.append(getExampleDynamicTableName(entityClass, defaultTableName));
        sql.append(" ");
        return sql.toString();
    }

    /**
     * Example查询中的where结构，用于只有一个Example参数时
     *
     * @return
     */
    public static String exampleWhereClause() {
        return "<if test=\"_parameter != null\"><where><trim prefixOverrides=\"and|or\">"
            + "${@com.merce.woven.common.mybatis.mapper.example.OGNL@andNotLogicDelete(_parameter)}"
            + loop(0, 9, null) + "</trim></where></if>";
    }

    /**
     * Example-Update中的where结构，用于多个参数时，Example带@Param("example")注解时
     *
     * @return
     */
    public static String updateByExampleWhereClause() {
        return "<if test=\"_parameter != null \"><where><trim prefixOverrides=\"and|or\" >"
            + "${@com.merce.woven.common.mybatis.mapper.example.OGNL@andNotLogicDelete(_parameter)}"
            + loop(0, 9, "example") + "</trim></where></if>";
    }

    private static String loop(int index, int max, String entityName) {
        int i = index + 1;
        StringBuffer buff = new StringBuffer();
        buff.append(" ${").append(exampleEntityName(entityName)).append("criteria").append(index > 0 ? index : "")
            .append(".andOr}");
        buff.append("<trim prefix=\"(\" prefixOverrides=\"and|or\" suffix=\")\">");
        buff.append("<foreach collection=\"").append(exampleEntityName(entityName)).append("criteria")
            .append(index > 0 ? index : "").append(".ordItems\" item=\"criteria").append(i + "\">");
        buff.append("<choose>");
        buff.append("<when test=\"criteria").append(i).append(".group\">");
        buff.append(index >= max ? "" : loop(index + 1, max, entityName));
        buff.append("</when>");
        buff.append("<otherwise>");
        buff.append("<choose>");
        buff.append("<when test=\"criteria").append(i).append(".noValue\"> ");
        buff.append(" ${criteria").append(i).append(".andOr} ").append(exampleTableAlias(entityName, true))
            .append("${criteria").append(i).append(".condition}");
        buff.append("</when>");
        buff.append("<when test=\"criteria").append(i).append(".singleValue\">");
        buff.append(" ${criteria").append(i).append(".andOr} ").append(exampleTableAlias(entityName, true))
            .append("${criteria").append(i).append(".condition} #{criteria").append(i).append(".value}");
        buff.append("</when>");
        buff.append("<when test=\"criteria").append(i).append(".betweenValue\">");
        buff.append(" ${criteria").append(i).append(".andOr} ").append(exampleTableAlias(entityName, true))
            .append("${criteria").append(i).append(".condition} #{criteria").append(i)
            .append(".value} and #{criteria").append(i).append(".secondValue}");
        buff.append("</when>");
        buff.append("<when test=\"criteria").append(i).append(".listValue\"> ");
        buff.append(" ${criteria").append(i).append(".andOr} ").append(exampleTableAlias(entityName, true))
            .append("${criteria").append(i).append(".condition} ");
        buff.append("<foreach close=\")\" collection=\"criteria").append(i)
            .append(".value\" item=\"listItem\" open=\"(\" separator=\",\">");
        buff.append("#{listItem}");
        buff.append("</foreach>");
        buff.append("</when>");
        buff.append("</choose>");
        buff.append("</otherwise>");
        buff.append("</choose>");
        buff.append("</foreach>");
        buff.append("</trim>");
        return buff.toString();
    }

    /**
     * Example 中包含至少 1 个查询条件
     *
     * @param parameterName 参数名
     * @return
     */
    public static String exampleHasAtLeastOneCriteriaCheck(String parameterName) {
        StringBuilder sql = new StringBuilder();
        sql.append(
            "<bind name=\"exampleHasAtLeastOneCriteriaCheck\" value=\"@com.merce.woven.common.mybatis.mapper.example.OGNL@exampleHasAtLeastOneCriteriaCheck(");
        sql.append(parameterName).append(")\"/>");
        return sql.toString();
    }

    /**
     * example支持查询指定列时
     *
     * @return
     */
    public static String exampleSelectColumns(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<choose>");
        sql.append("<when test=\"@com.merce.woven.common.mybatis.mapper.example.OGNL@hasSelectColumns(_parameter)\">");
        sql.append("<foreach collection=\"_parameter.selectColumns\" item=\"selectColumn\" separator=\",\">");
        sql.append(StringUtils.trim(exampleTableAlias(null, true))).append("${selectColumn}");
        sql.append("</foreach>");
        sql.append("</when>");
        // 不支持指定列的时候查询全部列
        sql.append("<otherwise>");
        sql.append(getAllColumns(entityClass));
        sql.append("</otherwise>");
        sql.append("</choose>");
        return sql.toString();
    }

    /**
     * 鑾峰彇鎵�鏈夋煡璇㈠垪锛屽id,name,code...
     *
     * @param entityClass
     * @return
     */
    public static String getAllColumns(Class<?> entityClass) {
        Set<EntityColumn> columnSet = EntityHelper.getColumns(entityClass);
        StringBuilder sql = new StringBuilder();
        for (EntityColumn entityColumn : columnSet) {
            sql.append(exampleTableAlias(null, true)).append(entityColumn.getColumn()).append(",");
        }
        return sql.substring(0, sql.length() - 1);
    }

    /**
     * example支持查询指定列时
     *
     * @return
     */
    public static String exampleCountColumn(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<choose>");
        sql.append("<when test=\"@com.merce.woven.common.mybatis.mapper.example.OGNL@hasCountColumn(_parameter)\">");
        sql.append("COUNT(<if test=\"distinct\">distinct </if>").append(exampleTableAlias(null, true))
            .append("${countColumn})");
        sql.append("</when>");
        sql.append("<otherwise>");
        sql.append("COUNT(*)");
        sql.append("</otherwise>");
        sql.append("</choose>");
        return sql.toString();
    }

    public static String exampleTableAlias(String entityName, boolean withDot) {
        StringBuffer buf = new StringBuffer();
        buf.append("<if test=\"").append(exampleEntityName(entityName)).append("tableAlias != null and ")
            .append(exampleEntityName(entityName)).append("tableAlias != '' \">").append(" ${")
            .append(exampleEntityName(entityName)).append("tableAlias}").append(withDot ? "." : "").append("</if>");
        return buf.toString();
    }

    public static String exampleEntityName(String entityName) {
        if (StringUtils.isNotEmpty(entityName)) {
            return entityName + ".";
        }
        return "";
    }

    /**
     * example查询中的orderBy条件，会判断默认orderBy
     *
     * @return
     */
    public static String exampleOrderBy(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"orderByClause != null\">");
        sql.append("order by ${orderByClause}");
        sql.append("</if>");
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            sql.append("<if test=\"orderByClause == null\">");
            sql.append("ORDER BY " + orderByClause);
            sql.append("</if>");
        }
        return sql.toString();
    }

    /**
     * example查询中的orderBy条件，会判断默认orderBy
     *
     * @return
     */
    public static String exampleOrderBy(String entityName, Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"").append(entityName).append(".orderByClause != null\">");
        sql.append("order by ${").append(entityName).append(".orderByClause}");
        sql.append("</if>");
        String orderByClause = EntityHelper.getOrderByClause(entityClass);
        if (orderByClause.length() > 0) {
            sql.append("<if test=\"").append(entityName).append(".orderByClause == null\">");
            sql.append("ORDER BY " + orderByClause);
            sql.append("</if>");
        }
        return sql.toString();
    }

    /**
     * example 支持 for update
     *
     * @return
     */
    public static String exampleForUpdate() {
        StringBuilder sql = new StringBuilder();
        sql.append("<if test=\"@com.merce.woven.common.mybatis.mapper.example.OGNL@hasForUpdate(_parameter)\">");
        sql.append("FOR UPDATE");
        sql.append("</if>");
        return sql.toString();
    }

    /**
     * example 支持 for update
     *
     * @return
     */
    public static String exampleCheck(Class<?> entityClass) {
        StringBuilder sql = new StringBuilder();
        sql.append(
            "<bind name=\"checkExampleEntityClass\" value=\"@com.merce.woven.common.mybatis.mapper.example.OGNL@checkExampleEntityClass(_parameter, '");
        sql.append(entityClass.getCanonicalName());
        sql.append("')\"/>");
        return sql.toString();
    }
}
