package com.info.baymax.dsp.access.dataapi.data.jdbc.condition;

import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.utils.ICollections;
import com.inforefiner.repackaged.org.apache.curator.shaded.com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@Slf4j
public abstract class AbstractQuerySql<Q> implements Serializable {
    private static final long serialVersionUID = 9076946654365840665L;

    protected AbstractQuerySql(String tableAlias, Q query) {
        this.tableAlias = tableAlias;
        build(query);
    }

    protected String tableAlias;

    public String getTableAlias() {
        return StringUtils.defaultIfEmpty(this.tableAlias, "");
    }

    public String getTableAliasAndDot() {
        return StringUtils.isEmpty(this.tableAlias) ? "" : (this.tableAlias + ".");
    }

    /**
     * 执行sql
     */
    @Getter
    @Setter
    protected String placeholderSql;

    /**
     * 参数值列表
     */
    @Getter
    protected Object[] paramValues = new Object[0];

    /**
     * 执行sql
     */
    protected String executeSql;

    /**
     * 是否合格的查询
     */
    @Getter
    protected boolean valid;

    abstract void build(Q query);

    protected boolean valid(Q query) {
        this.valid = true;
        return this.valid;
    }

    protected String selectAndWhere(List<String> finalSelectProperties, String table, String tableAlias,
                                    FieldGroup fieldGroup) {
        // select column1,column2,column3... from table
        StringBuffer buf = new StringBuffer();
        buf.append("select ").append(StringUtils.join(finalSelectProperties, ", ")).append(" from ").append(table)
            .append(" ").append(getTableAlias());

        // where 条件
        ConditionSql whereConditionSql = ConditionSql.build(tableAlias, fieldGroup);
        if (whereConditionSql != null && StringUtils.isNotEmpty(whereConditionSql.getPlaceholderSql())) {
            buf.append(" where ").append(StringUtils.trimToEmpty(whereConditionSql.getPlaceholderSql())).append(" ");
        }

        addParamValues(whereConditionSql.getParamValues());
        return buf.toString();
    }

    protected String orderBy(LinkedHashSet<Sort> sorts) {
        String orderBy = "";
        if (ICollections.hasElements(sorts)) {
            StringBuffer buf = new StringBuffer();
            buf.append(" ");
            for (Sort sort : sorts) {
                buf.append(sort.getName()).append(" ").append(sort.getOrder()).append(", ");
            }
            orderBy = StringUtils.stripEnd(buf.toString().trim(), ",");
            log.debug("orderBy: order by " + orderBy);
        }
        return orderBy;
    }

    public void addParamValues(Object[] values) {
        if (values != null && values.length > 0) {
            Object[] newValues = new Object[paramValues.length + values.length];
            System.arraycopy(paramValues, 0, newValues, 0, paramValues.length);
            System.arraycopy(values, 0, newValues, paramValues.length, values.length);
            this.paramValues = newValues;
        }
        log.debug("add param: " + Arrays.toString(values) + ",paramValues:" + Arrays.toString(paramValues));
    }

    public String getExecuteSql() {
        executeSql = placeholderSql;
        if (StringUtils.isNotEmpty(executeSql) && paramValues.length > 0) {
            for (Object obj : paramValues) {
                executeSql = executeSql.replaceFirst("\\?", parseObjectType(obj));
            }
        }
        return executeSql;
    }

    private boolean isArrayType(Object obj) {
        Class<?> clazz = obj.getClass();
        return clazz.isArray() || Collection.class.isAssignableFrom(clazz);
    }

    private String parseArrayValue(Object obj) {
        Class<?> clazz = obj.getClass();
        List<String> list = null;
        if (clazz.isArray()) {
            list = Lists.newArrayList();
            for (int i = 0; i < Array.getLength(obj); i++) {
                list.add(parseObjectType(Array.get(obj, i)));
            }
        } else {
            Collection<?> coll = (Collection<?>) obj;
            list = coll.stream().map(t -> parseObjectType(t)).collect(Collectors.toList());
        }
        return StringUtils.join(list, ",");
    }

    private String parseObjectType(Object obj) {
        if (isArrayType(obj)) {
            parseArrayValue(obj);
        } else if (isNumberType(obj)) {
            return parseNumberValue(obj);
        } else if (isBooleanType(obj)) {
            return parseBooleanValue(obj);
        } else if (isStringType(obj)) {
            return parseStringValue(obj);
        }
        return obj.toString();
    }

    private boolean isStringType(Object obj) {
        Class<?> clazz = obj.getClass();
        return CharSequence.class.isAssignableFrom(clazz) || Character.class.isAssignableFrom(clazz);
    }

    private String parseStringValue(Object obj) {
        return "'" + obj.toString() + "'";
    }

    private boolean isNumberType(Object obj) {
        Class<?> clazz = obj.getClass();
        return Number.class.isAssignableFrom(clazz);
    }

    private String parseNumberValue(Object obj) {
        return obj.toString();
    }

    private boolean isBooleanType(Object obj) {
        Class<?> clazz = obj.getClass();
        return clazz.equals(Boolean.class);
    }

    private String parseBooleanValue(Object obj) {
        return obj.toString();
    }
}
