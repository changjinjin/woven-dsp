package com.info.baymax.common.queryapi.sql;

import com.info.baymax.common.queryapi.query.field.Field;
import com.info.baymax.common.queryapi.query.field.FieldGroup;
import com.info.baymax.common.queryapi.query.field.FieldItem;
import com.info.baymax.common.queryapi.query.field.SqlEnums.AndOr;
import com.info.baymax.common.queryapi.query.field.SqlEnums.Operator;

import java.util.Arrays;
import java.util.List;

public class ConditionSql extends NamingSql {
    private int i = 0;

    public static ConditionSql build(FieldGroup fieldGroup) {
        ConditionSql conditionSql = new ConditionSql();
        String wherSql = conditionSql.where(fieldGroup);
        conditionSql.parse(wherSql, conditionSql.getParamMap());
        return conditionSql;
    }

    private String where(FieldGroup fieldGroup) {
        StringBuffer buf = new StringBuffer("");
        if (fieldGroup != null) {
            List<FieldItem> ordItems = fieldGroup.reIndex().ordItems();
            if (ordItems != null && !ordItems.isEmpty()) {
                for (FieldItem item : ordItems) {
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
        if (property == null || property.length() == 0) {
            return "";
            // throw new BizException(ErrType.BAD_REQUEST, "Error query field: empty property name.");
        }
        if (oper == null) {
            return "";
            // throw new BizException(ErrType.BAD_REQUEST, "Error query field: empty property oper.");
        }
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
        // String paramName = paramName();
        // paramMap.put(paramName, StringUtils.join(values, ","));
        StringBuffer buff = new StringBuffer();
        buff.append(" ").append(andOr).append(" ").append(property).append(" ").append(not).append("IN (");
        for (Object object : values) {
            String paramName = paramName();
            paramMap.put(paramName, object);
            buff.append("?").append(paramName).append(",");
        }
        buff = buff.replace(buff.length() - 1, buff.length(), "");
        return buff.append(")").toString();
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

    public static void main(String[] args) {
        // FieldGroup fieldGroup = FieldGroup.builder().andEqualTo("name", "zhangsan").andLike("name", "%li%")
        // .orGreaterThan("age", 12);
        // ConditionSql build = ConditionSql.build(fieldGroup);
        // System.out.println(build.placeholderSql);
        // System.out.println(Arrays.toString(build.paramValues));
        StringBuffer buff = new StringBuffer();
        buff.append("sdhdoieoijerojreoi").append(",");
        buff = buff.replace(buff.length() - 1, buff.length(), "");
        System.out.println(buff.toString());
    }

}
