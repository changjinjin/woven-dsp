package com.info.baymax.common.service.criteria.agg;

import java.util.Arrays;
import java.util.Collection;

import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.service.criteria.field.SqlEnums.OrderType;

/**
 * having排序查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2020年7月2日 下午2:24:38
 */
public interface HavingSortQueryBuilder<B extends HavingSortQueryBuilder<B>> {

    /**
     * having排序条件：批量集合添加
     *
     * @param sorts 排序属性对象
     * @return this builder
     */
    B havingSorts(Collection<Sort> sorts);

    /**
     * having排序条件：批量数组添加
     *
     * @param sorts 排序属性对象
     * @return this builder
     */
    default B havingSorts(Sort... sorts) {
        return havingSorts(Arrays.asList(sorts));
    }

    /**
     * having排序条件：单个添加
     *
     * @param sort 排序属性对象
     * @return this builder
     */
    default B havingSort(Sort sort) {
        return havingSorts(sort);
    }

    /**
     * having排序条件：添加排序字段
     *
     * @param fieldName 字段名称
     * @param asc       是否正序
     * @return this builder
     */
    default B havingOrderBy(String fieldName, boolean asc) {
        return havingSort(Sort.apply(fieldName, asc ? OrderType.ASC : OrderType.DESC));
    }

    /**
     * having排序条件：添加正序排序字段
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    default B havingOrderBy(String fieldName) {
        return havingOrderBy(fieldName, true);
    }

    /**
     * having排序条件：添加逆序排序字段
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    default B havingOrderByAsc(String fieldName) {
        return havingOrderBy(fieldName);
    }

    /**
     * having排序条件：添加正序排序字段数组
     *
     * @param fieldNames 多字段名称
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    default B havingOrderByAsc(String... fieldNames) {
        for (String fieldName : fieldNames) {
            havingOrderBy(fieldName);
        }
        return (B) this;
    }

    /**
     * having排序条件：添加逆序排序字段
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    default B havingOrderByDesc(String fieldName) {
        return havingOrderBy(fieldName, false);
    }

    /**
     * having排序条件：添加逆序排序字段数组
     *
     * @param fieldNames 多字段名称
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    default B havingOrderByDesc(String... fieldNames) {
        for (String fieldName : fieldNames) {
            havingOrderByDesc(fieldName);
        }
        return (B) this;
    }

    /**
     * 清空having排序条件
     *
     * @return this builder
     */
    B clearHavingSorts();

}
