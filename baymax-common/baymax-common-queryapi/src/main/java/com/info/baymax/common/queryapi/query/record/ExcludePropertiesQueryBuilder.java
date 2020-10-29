package com.info.baymax.common.queryapi.query.record;

import java.util.Arrays;
import java.util.Collection;

/**
 * 字段过滤查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface ExcludePropertiesQueryBuilder<B extends ExcludePropertiesQueryBuilder<B>> {

    /**
     * 添加不需要查询的字段列表
     *
     * @param excludeProperties 不需要查询的字段列表
     * @return this builder
     */
    B excludeProperties(Collection<String> excludeProperties);

    /**
     * 添加不需要查询的字段列表
     *
     * @param excludeProperties 不需要查询的字段列表
     * @return this builder
     */
    default B excludeProperties(String... excludeProperties) {
        return excludeProperties(Arrays.asList(excludeProperties));
    }

    /**
     * 添加不需要查询的字段
     *
     * @param excludeProperty 不需要查询的字段
     * @return this builder
     */
    default B excludeProperty(String excludeProperty) {
        return excludeProperties(excludeProperty);
    }

    /**
     * 清空ExcludeProperties条件
     *
     * @return this builder
     */
    B clearExcludeProperties();

}
