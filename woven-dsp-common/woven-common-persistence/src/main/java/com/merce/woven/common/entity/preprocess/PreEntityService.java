package com.merce.woven.common.entity.preprocess;

import com.merce.woven.common.service.BasePreprocessService;

/**
 * 定义一个预处理实例的处理接口类
 *
 * @param <T> 继承{@link PreEntity}的类
 * @author jingwei.yang
 * @date 2019年9月27日 下午3:36:02
 */
public interface PreEntityService<T extends PreEntity> extends BasePreprocessService<T> {

    /**
     * 插入之前的操作
     */
    default void preInsert(T t) {
        t.preInsert();
    }

    /**
     * 修改之前的操作
     */
    default void preUpdate(T t) {
        t.preUpdate();
    }

}
