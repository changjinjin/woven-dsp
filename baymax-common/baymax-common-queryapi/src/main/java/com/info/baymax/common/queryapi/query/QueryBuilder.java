package com.info.baymax.common.queryapi.query;

import java.util.Collection;

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
        if (addList != null && !addList.isEmpty()) {
            addList.clear();
        }
    }
}
