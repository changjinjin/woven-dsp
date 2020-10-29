package com.info.baymax.common.queryapi.query.aggregate;

import java.util.Arrays;
import java.util.Collection;

/**
 * 分页查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface GroupByFieldQueryBuilder<B extends GroupByFieldQueryBuilder<B>> {

    /**
     * 分组条件：添加分组字段集合
     *
     * @param fieldNames 多个分组字段
     * @return this builder
     */
    B groupFields(Collection<String> fieldNames);

    /**
     * 分组条件：添加分组字段数组
     *
     * @param fieldNames 多个分组字段
     * @return this builder
     */
    default B groupFields(String... fieldNames) {
        return groupFields(Arrays.asList(fieldNames));
    }

    /**
     * 分组条件：添加分组字段
     *
     * @param fieldName 分组字段
     * @return this builder
     */
    default B groupField(String fieldName) {
        return groupFields(fieldName);
    }

    /**
     * 分组条件：清空分组条件
     *
     * @return this builder
     */
    B clearGroupFields();

}
