package com.merce.woven.common.service.criteria.example;

import com.google.common.collect.Lists;
import com.merce.woven.common.service.criteria.example.SqlEnums.AndOr;
import com.merce.woven.common.service.criteria.example.SqlEnums.Operator;
import com.merce.woven.common.mybatis.mapper.example.Example;
import com.merce.woven.common.mybatis.mapper.example.Example.Criteria;
import com.merce.woven.common.mybatis.mapper.example.Example.CriteriaItem;
import com.merce.woven.common.utils.ICollections;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.EntityColumn;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CriteriaQuery 辅助器
 *
 * @author jingwei.yang
 * @date 2019年7月1日 下午5:07:38
 */
public class ExampleHelper {

    public static Example createExample(ExampleQuery query) {
        return createExample(query, query.getEntityClass());
    }

    /**
     * 根据条件创建Example对象
     *
     * @param query       查询条件
     * @param entityClass 查询的类型
     * @return Example对象
     */
    public static Example createExample(ExampleQuery query, Class<?> entityClass) {
        if (entityClass == null && (query == null || query.getEntityClass() == null)) {
            throw new IllegalArgumentException("查询的实体类型不能为空");
        }

        if (entityClass != null && query == null) {
            return new Example(entityClass);
        }

        if (query.getEntityClass() != null) {
            entityClass = query.getEntityClass();
        }
        Example example = new Example(entityClass);

        // 去重
        example.setDistinct(query.isDistinct());
        // 锁表
        example.setForUpdate(query.isForUpdate());

        // 计数字段
        String countProperty = query.getCountProperty();
        if (StringUtils.isNotEmpty(countProperty)) {
            example.setCountProperty(countProperty);
        }

        // 查询的字段
        Set<String> selectProperties = query.getSelectProperties();
        if (ICollections.hasElements(selectProperties)) {
            example.selectProperties(selectProperties.stream().toArray(String[]::new));
        }

        // 排除的字段
        Set<String> excludeProperties = query.getExcludeProperties();
        if (ICollections.hasElements(excludeProperties)) {
            example.excludeProperties(excludeProperties.stream().toArray(String[]::new));
        }

        // 是否动态表名
        if (StringUtils.isNotEmpty(query.getDynamicTable())) {
            example.setDynamicTable(query.getDynamicTable());
        }

        // 是否追加表名
        if (StringUtils.isNotEmpty(query.getAppendTable())) {
            example.setAppendTable(query.getAppendTable());
        }

        // 是否追加表名
        if (StringUtils.isNotEmpty(query.getTableAlias())) {
            example.setTableAlias(query.getTableAlias());
        }

        // 条件组合
        FieldGroup fieldGroup = query.getFieldGroup();
        if (fieldGroup != null) {
            createCriteria(example, fieldGroup);
        }

        // 排序
        example.setOrderByClause(createOrderByClause(example.getPropertyMap(), query.getOrdSort()));

        return example;
    }

    private static void createCriteria(Example example, FieldGroup fieldGroup) {
        if (fieldGroup != null) {
            example.setCriteria(from(example.criteria(), fieldGroup));
        }
    }

    public static Criteria from(Criteria criteria, FieldGroup fieldGroup) {
        if (fieldGroup == null) {
            return criteria;
        }
        criteria.setAndOr(fieldGroup.getAndOr().name());

        List<CriteriaItem> ordItems = fieldGroup.ordItems();
        if (ICollections.hasNoElements(ordItems)) {
            return criteria;
        }

        for (CriteriaItem item : ordItems) {
            if (item instanceof Field) {
                Field field = (Field) item;
                Operator operator = field.getOper();
                AndOr andOr = field.getAndOr();
                String name = field.getName();
                Object[] value = field.getValue();
                switch (andOr) {
                    case OR:
                        switch (operator) {
                            case EQUAL:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orEqualTo(name, value[0]);
                                break;
                            case NOT_EQUAL:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orNotEqualTo(name, value[0]);
                                break;
                            case LIKE:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orLike(name, value[0].toString());
                                break;
                            case NOT_LIKE:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orNotLike(name, value[0].toString());
                                break;
                            case BETWEEN:
                                validateFieldValue(name, operator, value, 2);
                                criteria.orBetween(name, value[0], value[1]);
                                break;
                            case NOT_BETWEEN:
                                validateFieldValue(name, operator, value, 2);
                                criteria.orNotBetween(name, value[0], value[1]);
                                break;
                            case GREATER_THAN:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orGreaterThan(name, value[0]);
                                break;
                            case GREATER_THAN_OR_EQUAL:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orGreaterThanOrEqualTo(name, value[0]);
                                break;
                            case LESS_THAN:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orLessThan(name, value[0]);
                                break;
                            case LESS_THAN_OR_EQUAL:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orLessThanOrEqualTo(name, value[0]);
                                break;
                            case IS_NULL:
                                criteria.orIsNull(name);
                                break;
                            case NOT_NULL:
                                criteria.orIsNotNull(name);
                                break;
                            case IN:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orIn(name, Lists.newArrayList(value));
                                break;
                            case NOT_IN:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orNotIn(name, Lists.newArrayList(value));
                                break;
                            default:
                                validateFieldValue(name, operator, value, 1);
                                criteria.orEqualTo(name, value);
                                break;
                        }
                        break;
                    default:
                        switch (operator) {
                            case EQUAL:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andEqualTo(name, value[0]);
                                break;
                            case NOT_EQUAL:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andNotEqualTo(name, value[0]);
                                break;
                            case LIKE:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andLike(name, value[0].toString());
                                break;
                            case NOT_LIKE:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andNotLike(name, value[0].toString());
                                break;
                            case BETWEEN:
                                validateFieldValue(name, operator, value, 2);
                                criteria.andBetween(name, value[0], value[1]);
                                break;
                            case NOT_BETWEEN:
                                validateFieldValue(name, operator, value, 2);
                                criteria.andNotBetween(name, value[0], value[1]);
                                break;
                            case GREATER_THAN:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andGreaterThan(name, value[0]);
                                break;
                            case GREATER_THAN_OR_EQUAL:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andGreaterThanOrEqualTo(name, value[0]);
                                break;
                            case LESS_THAN:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andLessThan(name, value[0]);
                                break;
                            case LESS_THAN_OR_EQUAL:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andLessThanOrEqualTo(name, value[0]);
                                break;
                            case IS_NULL:
                                criteria.andIsNull(name);
                                break;
                            case NOT_NULL:
                                criteria.andIsNotNull(name);
                                break;
                            case IN:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andIn(name, Lists.newArrayList(value));
                                break;
                            case NOT_IN:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andNotIn(name, Lists.newArrayList(value));
                                break;
                            default:
                                validateFieldValue(name, operator, value, 1);
                                criteria.andEqualTo(name, value);
                                break;
                        }
                        break;
                }
            } else {
                criteria.criteria(
                    from(new Criteria(criteria.getPropertyMap(), criteria.isExists(), criteria.isNotNull()),
                        (FieldGroup) item));
            }
        }
        return criteria;
    }

    private static void validateFieldValue(String name, Operator comparatorOperator, Object[] fieldValue,
                                           int minLength) {
        if (fieldValue == null) {
            throw new IllegalArgumentException(
                String.format("属性[%s]进行[%s]关系比较时，参数不能为空，且参数至少有%d个", name, comparatorOperator.name(), minLength));
        }

        if (fieldValue.length < minLength) {
            throw new IllegalArgumentException(String.format("属性[%s]进行[%s]关系比较时，至少要有%d个参数，数组%s长度与要求不符", name,
                comparatorOperator.name(), minLength, Arrays.toString(fieldValue)));
        }
    }

    private static String createOrderByClause(Map<String, EntityColumn> propertyMap, List<Sort> ordSort) {
        if (ICollections.hasNoElements(ordSort)) {
            return null;
        }

        StringBuffer buff = new StringBuffer();
        for (Sort sort : ordSort) {
            String fieldName = sort.getName();
            String columnName = getPropertyColumn(propertyMap, fieldName);
            if (StringUtils.isNotEmpty(columnName)) {
                buff.append(columnName).append(" ").append(sort.getOrder().name()).append(",");
            }
        }

        String orderByClause = buff.toString();
        if (StringUtils.isNotEmpty(orderByClause) && orderByClause.endsWith(",")) {
            return orderByClause.substring(0, orderByClause.length() - 1);
        }
        return null;
    }

    private static String getPropertyColumn(Map<String, EntityColumn> propertyMap, String fieldName) {
        if (propertyMap.containsKey(fieldName)) {
            return propertyMap.get(fieldName).getColumn();
        }
        return null;
    }

}
