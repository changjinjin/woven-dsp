package com.merce.woven.common.mybatis.mapper.aggregation;

import java.text.MessageFormat;
import java.util.Map;

import org.apache.ibatis.mapping.MappedStatement;

import com.merce.woven.common.mybatis.mapper.example.ExampleSqlHelper;

import tk.mybatis.mapper.entity.EntityColumn;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.Assert;
import tk.mybatis.mapper.util.SqlReservedWords;
import tk.mybatis.mapper.util.StringUtil;

/**
 * @author liuchan
 * @author liuzh
 */
public class AggregationProvider extends MapperTemplate {

    public AggregationProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public static String aggregationSelectClause(Class<?> entityClass, String wrapKeyword,
                                                 AggregateCondition condition) {
        Assert.notEmpty(condition.getAggregateProperty(),
            "aggregateProperty must have length; it must not be null or empty");
        Assert.notNull(condition.getAggregateType(), "aggregateType is required; it must not be null");
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        Map<String, EntityColumn> propertyMap = entityTable.getPropertyMap();
        StringBuilder selectBuilder = new StringBuilder();
        selectBuilder.append(condition.getAggregateType().name());
        String columnName = propertyMap.get(condition.getAggregateProperty()).getColumn();
        selectBuilder.append("(").append(columnName).append(")");
        selectBuilder.append(" AS ");
        if (StringUtil.isNotEmpty(condition.getAggregateAliasName())) {
            selectBuilder.append(condition.getAggregateAliasName());
        } else {
            selectBuilder.append(wrapKeyword(wrapKeyword, columnName));
        }
        if (condition.getGroupByProperties() != null && condition.getGroupByProperties().size() > 0) {
            for (String property : condition.getGroupByProperties()) {
                selectBuilder.append(", ");
                columnName = propertyMap.get(property).getColumn();
                selectBuilder.append(columnName).append(" AS ");

                // 判断是否买指定了分组字段的别名，如果指定了别名则使用别名
                if (condition.getGroupByProperty(property) == null) {
                    selectBuilder.append(wrapKeyword(wrapKeyword, columnName));
                } else {
                    selectBuilder.append(condition.getGroupByProperty(property));
                }
            }
        }
        return selectBuilder.toString();
    }

    private static String wrapKeyword(String wrapKeyword, String columnName) {
        if (StringUtil.isNotEmpty(wrapKeyword) && SqlReservedWords.containsWord(columnName)) {
            return MessageFormat.format(wrapKeyword, columnName);
        }
        return columnName;
    }

    public static String aggregationGroupBy(Class<?> entityClass, String wrapKeyword, AggregateCondition condition) {
        if (condition.getGroupByProperties() != null && condition.getGroupByProperties().size() > 0) {
            EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
            Map<String, EntityColumn> propertyMap = entityTable.getPropertyMap();
            StringBuilder groupByBuilder = new StringBuilder();
            for (String property : condition.getGroupByProperties()) {
                if (groupByBuilder.length() == 0) {
                    groupByBuilder.append(" GROUP BY ");
                } else {
                    groupByBuilder.append(", ");
                }
                groupByBuilder.append(propertyMap.get(property).getColumn());
            }
            return groupByBuilder.toString();
        }
        return "";
    }

    /**
     * 根据Example查询总数
     *
     * @param ms
     * @return
     */
    public String selectAggregationByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (isCheckExampleEntityClass()) {
            sql.append(ExampleSqlHelper.exampleCheck(entityClass));
        }
        sql.append("SELECT ${@com.merce.woven.common.mybatis.mapper.aggregation.AggregationProvider@aggregationSelectClause(");
        sql.append("@").append(entityClass.getCanonicalName()).append("@class");
        sql.append(", '").append(getConfig().getWrapKeyword()).append("'");
        sql.append(", aggregateCondition");
        sql.append(")} ");
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(ExampleSqlHelper.updateByExampleWhereClause());
        sql.append(" ${@com.merce.woven.common.mybatis.mapper.aggregation.AggregationProvider@aggregationGroupBy(");
        sql.append("@").append(entityClass.getCanonicalName()).append("@class");
        sql.append(", '").append(getConfig().getWrapKeyword()).append("'");
        sql.append(", aggregateCondition");
        sql.append(")} ");
        sql.append(ExampleSqlHelper.exampleOrderBy("example", entityClass));
        sql.append(ExampleSqlHelper.exampleForUpdate());
        return sql.toString();
    }

}
