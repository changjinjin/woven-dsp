package com.info.baymax.dsp.access.dataapi.data.jdbc.sql;

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

    protected AbstractQuerySql(Q query) {
        build(query);
    }

    /**
     * 执行sql
     */
    @Getter
    @Setter
    protected String placeholderCountSql;

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
     * 计数sql
     */
    protected String countSql;

    /**
     * 执行sql
     */
    protected String executeSql;

    /**
     * 是否合格的查询
     */
    @Getter
    protected boolean valid;

    protected abstract void build(Q query);

    protected boolean valid(Q query) {
        this.valid = true;
        return this.valid;
    }

    protected String properties(Q query) {
        return " " + StringUtils.join(getSelectProperties(query), ", ");
    }

    protected String count() {
        return " COUNT(*)";
    }

    protected String from(String table) {
        return new StringBuffer().append(" FROM ").append(table).toString();
    }

    protected String where(FieldGroup fieldGroup) {
        // where 条件
        StringBuffer buf = new StringBuffer();
        ConditionSql whereConditionSql = ConditionSql.build(fieldGroup);
        if (whereConditionSql != null && StringUtils.isNotEmpty(whereConditionSql.getPlaceholderSql())) {
            buf.append(" WHERE ").append(StringUtils.trimToEmpty(whereConditionSql.getPlaceholderSql()));
        }
        addParamValues(whereConditionSql.getParamValues());
        return buf.toString();
    }

    protected String selectfromTableWhere(String table, FieldGroup fieldGroup) {
        // select column1,column2,column3... from table
        return new StringBuffer().append("SELECT ").append(" %s ").append(from(table)).append(where(fieldGroup))
            .toString();
    }

    protected String orderBy(LinkedHashSet<Sort> sorts) {
        StringBuffer buf = new StringBuffer();
        if (ICollections.hasElements(sorts)) {
            buf.append(" ORDER BY ");
            for (Sort sort : sorts) {
                buf.append(sort.getName()).append(" ").append(sort.getOrder()).append(", ");
            }
            return " " + StringUtils.stripEnd(buf.toString().trim(), ",");
        }
        return "";
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

    public String getCountSql() {
        countSql = placeholderCountSql;
        if (StringUtils.isNotEmpty(countSql) && paramValues.length > 0) {
            for (Object obj : paramValues) {
                countSql = countSql.replaceFirst("\\?", parseObjectType(obj));
            }
        }
        return countSql;
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

    protected abstract List<String> getSelectProperties(Q query);
}
