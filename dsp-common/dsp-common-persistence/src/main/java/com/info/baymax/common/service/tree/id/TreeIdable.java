package com.info.baymax.common.service.tree.id;

import com.info.baymax.common.service.tree.Treeable;

import java.io.Serializable;

/**
 * 有主键的树结构数据类型接口
 *
 * @param <ID> 主键类型，即关系维护的字段类型
 * @param <T>  实体类型
 * @author jingwei.yang
 * @date 2019年9月24日 下午5:20:24
 */
public interface TreeIdable<ID extends Serializable, T extends TreeIdable<ID, T>> extends Treeable<T> {

    /**
     * 获取ID
     *
     * @return ID
     */
    ID getId();

    /**
     * 获取父节点ID
     *
     * @return 父节点ID
     */
    ID getParentId();
}
