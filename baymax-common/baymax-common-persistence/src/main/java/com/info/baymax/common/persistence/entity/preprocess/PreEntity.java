package com.info.baymax.common.persistence.entity.preprocess;

/**
 * 定义一个预处理实例的接口类型
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午3:26:49
 */
public interface PreEntity {

    /**
     * 插入之前的操作
     */
    default void preInsert() {
    }

    /**
     * 修改之前的操作
     */
    default void preUpdate() {
    }

}
