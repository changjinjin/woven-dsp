package com.info.baymax.common.persistence.service.criteria.example;

/**
 * 字段过滤查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface ForUpdateQueryBuilder<B extends ForUpdateQueryBuilder<B>> {

    /**
     * 是否锁表查询，一般无需指定
     *
     * @param forUpdate 是否锁表，默认false
     * @return this builder
     */
    B forUpdate(boolean forUpdate);

    /**
     * 清空forUpdate条件
     *
     * @return this builder
     */
    B clearForUpdate();

}
