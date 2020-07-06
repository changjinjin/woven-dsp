package com.info.baymax.common.service.criteria.query;

import java.util.Collection;

import com.info.baymax.common.utils.ICollections;

/**
 * 查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface QueryBuilder<B extends QueryBuilder<B>> {

    /**
     * 清空条件属性
     *
     * @return this builder
     */
    B clear();

    default <T> void clear(Collection<T> addList) {
        if (ICollections.hasElements(addList)) {
            addList.clear();
        }
    }
}
