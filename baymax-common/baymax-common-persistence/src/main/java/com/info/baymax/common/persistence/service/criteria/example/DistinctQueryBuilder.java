package com.info.baymax.common.persistence.service.criteria.example;

/**
 * 去重查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface DistinctQueryBuilder<B extends DistinctQueryBuilder<B>> {

    /**
     * 是否去重查询，一般无需指定，默认不去重
     *
     * @param distinct 是否去重，默认false
     * @return this builder
     */
    B distinct(boolean distinct);

    /**
     * 清空Distinct条件
     *
     * @return this builder
     */
    B clearDistinct();

}
