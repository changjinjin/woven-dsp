package com.info.baymax.common.queryapi.record;

import com.info.baymax.common.queryapi.field.FieldGroup;

/**
 * 查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2019年9月5日 下午3:10:55
 */
public interface FieldGroupQueryBuilder<B extends FieldGroupQueryBuilder<B>> {

    /**
     * 添加一个组合查询条件对象，默认根节点
     *
     * @return this builder
     */
    FieldGroup fieldGroup();

    /**
     * 添加一个组合查询条件对象
     *
     * @param fieldGroup 组合查询条件对象
     * @return this builder
     */
    B fieldGroup(FieldGroup fieldGroup);

    /**
     * 清空FieldGroup条件
     *
     * @return this builder
     */
    B clearFieldGroup();

}
