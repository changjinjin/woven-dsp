package com.info.baymax.common.persistence.entity.gene;

import java.io.Serializable;

/**
 * 记录有效状态
 *
 * @param <E> 启用状态类型
 * @author jingwei.yang
 * @date 2020年11月11日 下午8:15:49
 */
public interface Enabled<E extends Serializable> {

    /**
     * 获取启用状态
     *
     * @return 启用状态
     */
    E getEnabled();

    /**
     * 设置启用状态
     *
     * @param enabled 启用状态
     */
    void setEnabled(E enabled);

}
