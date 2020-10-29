package com.info.baymax.common.queryapi.query.aggregate;

import com.info.baymax.common.queryapi.query.field.FieldGroup;

/**
 * having查询条件构造器接口
 *
 * @param <B>
 * @author jingwei.yang
 * @date 2020年7月2日 下午2:23:43
 */
public interface HavingFieldGroupQueryBuilder<B extends HavingFieldGroupQueryBuilder<B>> {

    /**
     * 添加一个having查询条件对象，默认根节点
     *
     * @return this builder
     */
    FieldGroup havingFieldGroup();

    /**
     * 添加一个having查询条件对象
     *
     * @param fieldGroup 组合查询条件对象
     * @return this builder
     */
    B havingFieldGroup(FieldGroup fieldGroup);

    /**
     * 清空havingFieldGroup条件
     *
     * @return this builder
     */
    B clearHavingFieldGroup();

}
