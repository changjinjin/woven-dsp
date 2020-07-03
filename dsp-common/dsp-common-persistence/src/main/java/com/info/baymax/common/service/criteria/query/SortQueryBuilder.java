package com.info.baymax.common.service.criteria.query;

import java.util.Arrays;
import java.util.Collection;

import com.info.baymax.common.service.criteria.field.Sort;
import com.info.baymax.common.service.criteria.field.SqlEnums.OrderType;

/**
 * 排序查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface SortQueryBuilder<B extends SortQueryBuilder<B>> {

    /**
     * 设置多个排序字段
     *
     * @param ordSort 排序属性对象
     * @return this builder
     */
    B sorts(Collection<Sort> sorts);

    /**
     * 设置多个排序字段
     *
     * @param ordSort 排序属性对象
     * @return this builder
     */
    default B sorts(Sort... sorts) {
        return sorts(Arrays.asList(sorts));
    }

    /**
     * 添加排序字段
     *
     * @param sort 排序属性对象
     * @return this builder
     */
    default B sort(Sort sort) {
        return sorts(sort);
    }

    /**
     * 添加排序字段，按照字段正序排序
     *
     * @param fieldName 字段名称
     * @param asc       是否正序
     * @return this builder
     */
    default B orderBy(String fieldName, boolean asc) {
        return sort(Sort.apply(fieldName, asc ? OrderType.ASC : OrderType.DESC));
    }

    /**
     * 添加排序字段，按照字段正序排序
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    default B orderBy(String fieldName) {
        return orderBy(fieldName, true);
    }

    /**
     * 添加正序排序字段
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    default B orderByAsc(String fieldName) {
        return orderBy(fieldName);
    }

    /**
     * 添加正序排序字段
     *
     * @param fieldNames 多字段名称
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    default B orderByAsc(String... fieldNames) {
        for (String fieldName : fieldNames) {
            orderBy(fieldName);
        }
        return (B) this;
    }

    /**
     * 添加逆序排序字段
     *
     * @param fieldName 字段名称
     * @return this builder
     */
    default B orderByDesc(String fieldName) {
        return orderBy(fieldName, false);
    }

    /**
     * 添加逆序排序字段
     *
     * @param fieldNames 多字段名称
     * @return this builder
     */
    @SuppressWarnings("unchecked")
    default B orderByDesc(String... fieldNames) {
        for (String fieldName : fieldNames) {
            orderByDesc(fieldName);
        }
        return (B) this;
    }

    /**
     * 清空Sorts条件
     *
     * @return this builder
     */
    B clearSorts();

}
