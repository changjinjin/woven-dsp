package com.info.baymax.dsp.access.dataapi.data.jdbc.sql;

import com.info.baymax.common.mybatis.mapper.example.Example.CriteriaItem;
import com.info.baymax.common.service.criteria.field.Field;
import com.info.baymax.common.service.criteria.field.FieldGroup;
import com.info.baymax.common.service.criteria.field.SqlEnums.AndOr;
import com.info.baymax.common.service.criteria.field.SqlEnums.Operator;
import com.info.baymax.common.utils.ICollections;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

@ToString
public class ConditionSql implements Serializable {
    private static final long serialVersionUID = 7871946632377823592L;
    private FieldGroup fieldGroup;

    private int i = 0;

    private ConditionSql(FieldGroup fieldGroup) {
        this.fieldGroup = fieldGroup;
    }

    public static ConditionSql build(FieldGroup fieldGroup) {
        return new ConditionSql(fieldGroup).parse();
    }

    /**
     * 命名条件sql
     */
    @Getter
    private String namingSql;

    /**
     * 有占位符的sql
     */
    @Getter
    private String placeholderSql;

    /**
     * 参数名称列表
     */
    @Getter
    private String[] paramNames;

    /**
     * 参数值列表
     */
    @Getter
    private Object[] paramValues;

    /**
     * 参数名称与值映射
     */
    @Getter
    private Map<String, Object> paramMap = new HashMap<String, Object>();

    /**
     * 分析命名SQL语句获取抽象NSQl实例；java(JDBC)提供SQL语句命名参数而是通过?标识参数位置， 通过此对象可以命名参数方式使用SQL语句，命名参数以?开始后跟名称?name。 例如：SELECT * FROM
     * table WHERE name = ?name AND email = ?email;
     */
    private ConditionSql parse() {
        namingSql = trimAndOr(where(fieldGroup));
        char c;
        List<String> names = new ArrayList<String>();
        StringBuilder sql_builder = new StringBuilder();
        StringBuilder name_builder = new StringBuilder();
        for (int index = 0; index < namingSql.length(); index++) {
            c = namingSql.charAt(index);
            sql_builder.append(c);
            if ('?' == c) {
                while (++index < namingSql.length()) {
                    c = namingSql.charAt(index);
                    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_' || (c >= '0' && c <= '9')) {
                        name_builder.append(c);
                    } else {
                        sql_builder.append(c);
                        break;
                    }
                }
                names.add(name_builder.toString());
                name_builder.setLength(0);
            }
        }
        placeholderSql = sql_builder.toString();
        if (names != null && names.size() > 0) {
            paramNames = names.toArray(paramNames = new String[names.size()]);
            int mapSize = paramMap.size();
            if (names.size() != mapSize) {
                throw new IllegalArgumentException(
                    "Wrong number of parameters: expected " + names.size() + ", was given " + mapSize);
            }
            paramValues = new Object[mapSize];
            for (int i = 0; i < paramValues.length; i++) {
                paramValues[i] = paramMap.get(names.get(i));
            }
        }
        return this;
    }

    private String where(FieldGroup fieldGroup) {
        StringBuffer buf = new StringBuffer("");
        if (fieldGroup != null) {
            List<CriteriaItem> ordItems = fieldGroup.reIndex().ordItems();
            if (ICollections.hasElements(ordItems)) {
                for (CriteriaItem item : ordItems) {
                    if (item instanceof FieldGroup) {
                        FieldGroup group = (FieldGroup) item;
                        buf.append(" ").append(group.getAndOr().name().toLowerCase()).append(" (")
                            .append(trimAndOr(where(group))).append(")");
                    } else {
                        Field field = (Field) item;
                        buf.append(field(field));
                    }
                }
            }
        }
        return buf.toString();
    }

    private String field(Field field) {
        AndOr andOr = field.getAndOr();
        String property = field.getName();
        Operator oper = field.getOper();
        Object[] values = field.getValue();
        switch (andOr) {
            case OR:
                switch (oper) {
                    case EQUAL:
                        return orEqualTo(property, values[0]);
                    case NOT_EQUAL:
                        return orNotEqualTo(property, values[0]);
                    case LIKE:
                        return orLike(property, values[0]);
                    case NOT_LIKE:
                        return orNotLike(property, values[0]);
                    case BETWEEN:
                        return orBetween(property, values[0], values[1]);
                    case NOT_BETWEEN:
                        return orNotBetween(property, values[0], values[1]);
                    case GREATER_THAN:
                        return orGreaterThan(property, values[0]);
                    case GREATER_THAN_OR_EQUAL:
                        return orGreaterThanOrEqualTo(property, values[0]);
                    case LESS_THAN:
                        return orLessThan(property, values[0]);
                    case LESS_THAN_OR_EQUAL:
                        return orLessThanOrEqualTo(property, values[0]);
                    case IS_NULL:
                        return orIsNull(property);
                    case NOT_NULL:
                        return orIsNotNull(property);
                    case IN:
                        return orIn(property, Arrays.asList(values));
                    case NOT_IN:
                        return orNotIn(property, Arrays.asList(values));
                    default:
                        return orEqualTo(property, values[0]);
                }
            default:
                switch (oper) {
                    case EQUAL:
                        return andEqualTo(property, values[0]);
                    case NOT_EQUAL:
                        return andNotEqualTo(property, values[0]);
                    case LIKE:
                        return andLike(property, values[0]);
                    case NOT_LIKE:
                        return andNotLike(property, values[0]);
                    case BETWEEN:
                        return andBetween(property, values[0], values[1]);
                    case NOT_BETWEEN:
                        return andNotBetween(property, values[0], values[1]);
                    case GREATER_THAN:
                        return andGreaterThan(property, values[0]);
                    case GREATER_THAN_OR_EQUAL:
                        return andGreaterThanOrEqualTo(property, values[0]);
                    case LESS_THAN:
                        return andLessThan(property, values[0]);
                    case LESS_THAN_OR_EQUAL:
                        return andLessThanOrEqualTo(property, values[0]);
                    case IS_NULL:
                        return andIsNull(property);
                    case NOT_NULL:
                        return andIsNotNull(property);
                    case IN:
                        return andIn(property, Arrays.asList(values));
                    case NOT_IN:
                        return andNotIn(property, Arrays.asList(values));
                    default:
                        return andEqualTo(property, values[0]);
                }
        }
    }

    public String paramName() {
        return "param" + i++;
    }

    private String condition(String andOr, String property, String operator, Object value) {
        String paramName = paramName();
        paramMap.put(paramName, value);
        return new StringBuffer().append(" ").append(andOr).append(" ").append(property).append(" ").append(operator)
            .append(" ?").append(paramName).toString();
    }

    private String andCondition(String property, String operator, Object value) {
        return condition("AND", property, operator, value);
    }

    private String orCondition(String property, String operator, Object value) {
        return condition("OR", property, operator, value);
    }

    private String betweenCondition(String andOr, String property, String not, Object value1, Object value2) {
        String paramName1 = paramName();
        paramMap.put(paramName1, value1);
        String paramName2 = paramName();
        paramMap.put(paramName2, value2);
        StringBuffer buf = new StringBuffer();
        buf.append(" ").append(andOr).append(" ").append(property).append(" ").append(not).append(" ")
            .append("BETWEEN ?").append(paramName1).append(" AND ?").append(paramName2);
        return buf.toString();
    }

    private String inCondition(String andOr, String property, String not, Iterable<?> values) {
        String paramName = paramName();
        paramMap.put(paramName, StringUtils.join(values, ","));
        return new StringBuffer().append(" ").append(andOr).append(" ").append(property).append(" ").append(not)
            .append("IN (?").append(paramName).append(")").toString();
    }

    private String trimAndOr(String sql) {
        return StringUtils.stripStart(StringUtils.stripStart(StringUtils.trim(sql), "AND"), "OR").trim();
    }

    private String andIsNull(String property) {
        return " AND " + property + " IS NULL";
    }

    private String andIsNotNull(String property) {
        return " AND " + property + " IS NOT NULL";
    }

    private String andEqualTo(String property, Object value) {
        return andCondition(property, "=", value);
    }

    private String andNotEqualTo(String property, Object value) {
        return andCondition(property, "<>", value);
    }

    private String andGreaterThan(String property, Object value) {
        return andCondition(property, ">", value);
    }

    private String andGreaterThanOrEqualTo(String property, Object value) {
        return andCondition(property, ">=", value);
    }

    private String andLessThan(String property, Object value) {
        return andCondition(property, "<", value);
    }

    private String andLessThanOrEqualTo(String property, Object value) {
        return andCondition(property, "<=", value);
    }

    private String andIn(String property, Iterable<?> values) {
        return inCondition("AND", property, "", values);
    }

    private String andNotIn(String property, Iterable<?> values) {
        return inCondition("AND", property, "NOT ", values);
    }

    private String andBetween(String property, Object value1, Object value2) {
        return betweenCondition("AND", property, "", value1, value2);
    }

    private String andNotBetween(String property, Object value1, Object value2) {
        return betweenCondition("AND", property, "NOT ", value1, value2);
    }

    private String andLike(String property, Object value) {
        return andCondition(property, "LIKE", value);
    }

    private String andNotLike(String property, Object value) {
        return andCondition(property, "NOT LIKE", value);
    }

    private String orIsNull(String property) {
        return " OR " + property + " IS NULL";
    }

    private String orIsNotNull(String property) {
        return " OR " + property + " IS NOT NULL";
    }

    private String orEqualTo(String property, Object value) {
        return orCondition(property, "=", value);
    }

    private String orNotEqualTo(String property, Object value) {
        return orCondition(property, "<>", value);
    }

    private String orGreaterThan(String property, Object value) {
        return orCondition(property, ">", value);
    }

    private String orGreaterThanOrEqualTo(String property, Object value) {
        return orCondition(property, ">=", value);
    }

    private String orLessThan(String property, Object value) {
        return orCondition(property, "<", value);
    }

    private String orLessThanOrEqualTo(String property, Object value) {
        return orCondition(property, "<=", value);
    }

    private String orIn(String property, Iterable<?> values) {
        return inCondition("OR", property, "", values);
    }

    private String orNotIn(String property, Iterable<?> values) {
        return inCondition("OR", property, "NOT ", values);
    }

    private String orBetween(String property, Object value1, Object value2) {
        return betweenCondition("OR", property, "", value1, value2);
    }

    private String orNotBetween(String property, Object value1, Object value2) {
        return betweenCondition("OR", property, "NOT ", value1, value2);
    }

    private String orLike(String property, Object value) {
        return orCondition(property, "LIKE", value);
    }

    private String orNotLike(String property, Object value) {
        return orCondition(property, "NOT LIKE", value);
    }
}
