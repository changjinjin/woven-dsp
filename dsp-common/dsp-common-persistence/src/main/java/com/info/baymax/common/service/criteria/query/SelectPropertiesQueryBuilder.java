package com.info.baymax.common.service.criteria.query;

import java.util.Arrays;
import java.util.Collection;

/**
 * 字段过滤查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface SelectPropertiesQueryBuilder<B extends SelectPropertiesQueryBuilder<B>> {

    /**
     * 添加需要查询的字段列表
     *
     * @param selectProperties 需要查询的字段列表
     * @return this builder
     */
    B selectProperties(Collection<String> selectProperties);

    /**
     * 添加需要查询的字段列表
     *
     * @param selectProperties 需要查询的字段列表
     * @return this builder
     */
    default B selectProperties(String... selectProperties) {
        return selectProperties(Arrays.asList(selectProperties));
    }

    /**
     * 添加需要查询的字段
     *
     * @param selectProperty 需要查询的字段
     * @return this builder
     */
    default B selectProperty(String selectProperty) {
        return selectProperties(selectProperty);
    }

    /**
     * 清空SelectProperties条件
     *
     * @return this builder
     */
    B clearSelectProperties();

}
