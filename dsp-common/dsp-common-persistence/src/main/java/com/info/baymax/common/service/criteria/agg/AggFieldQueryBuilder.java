package com.info.baymax.common.service.criteria.agg;

import java.util.Arrays;
import java.util.Collection;

/**
 * 聚合条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface AggFieldQueryBuilder<B extends AggFieldQueryBuilder<B>> {

    /**
     * 聚合条件：添加字段集合
     *
     * @param aggFields 多个分组字段
     * @return this builder
     */
    B aggFields(Collection<AggField> aggFields);

    /**
     * 聚合条件：添加字段数组
     *
     * @param aggFields 多个分组字段
     * @return this builder
     */
    default B aggFields(AggField... aggFields) {
        return aggFields(Arrays.asList(aggFields));
    }

    /**
     * 聚合条件：添加字段
     *
     * @param aggField 聚合字段
     * @return this builder
     */
    default B aggField(AggField aggField) {
        return aggFields(aggField);
    }

    /**
     * 聚合条件：添加字段
     *
     * @param name     聚合字段
     * @param alias    聚合别名
     * @param aggType  聚合类型
     * @param distinct 是否去重
     * @return this builder
     */
    default B aggField(String name, String alias, AggType aggType, boolean distinct) {
        return aggFields(AggField.builder().name(name).alias(alias).aggType(aggType).distinct(distinct).build());
    }

    /**
     * 聚合条件：添加字段
     *
     * @param name    聚合字段
     * @param alias   聚合别名
     * @param aggType 聚合类型
     * @return this builder
     */
    default B aggField(String name, String alias, AggType aggType) {
        return aggField(name, alias, aggType, false);
    }

    /**
     * 聚合条件：添加字段
     *
     * @param name     聚合字段
     * @param aggType  聚合类型
     * @param distinct 是否去重
     * @return this builder
     */
    default B aggField(String name, AggType aggType, boolean distinct) {
        return aggField(name, aggType.getValue() + "_" + name, aggType, distinct);
    }

    /**
     * 聚合条件：添加字段
     *
     * @param name    聚合字段
     * @param aggType 聚合类型
     * @return this builder
     */
    default B aggField(String name, AggType aggType) {
        return aggField(name, aggType, false);
    }

    /**
     * 清空聚合条件
     *
     * @return this builder
     */
    B clearAggFields();

}
