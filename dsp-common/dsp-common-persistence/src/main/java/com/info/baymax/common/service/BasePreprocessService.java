package com.info.baymax.common.service;

import com.info.baymax.common.utils.ICollections;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 实体业务预处理接口类，主要用于实体的数据库插入与更新预处理操作
 *
 * @author jingwei.yang
 * @date 2019-05-29 14:57
 */
@Transactional
public interface BasePreprocessService<T> {

    /**
     * 插入之前的预处理操作
     */
    default void preInsert(T t) {
        // default do nothing
    }

    /**
     * 插入之前的预处理操作
     */
    default void preInsert(List<? extends T> list) {
        if (ICollections.hasElements(list)) {
            for (T t : list) {
                preInsert(t);
            }
        }
    }

    /**
     * 修改之前的预处理操作
     */
    default void preUpdate(T t) {
        // default do nothing
    }

    /**
     * 修改之前的预处理操作
     */
    default void preUpdate(List<? extends T> list) {
        if (ICollections.hasElements(list)) {
            for (T t : list) {
                preUpdate(t);
            }
        }
    }
}
