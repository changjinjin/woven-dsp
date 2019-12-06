package com.merce.woven.common.base.tree.id;

import com.merce.woven.common.base.tree.Treeable;

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
     * 设置ID
     *
     * @param id ID
     */
    void setId(ID id);

    /**
     * 获取父节点ID
     *
     * @return 父节点ID
     */
    ID getParentId();

    /**
     * 设置父节点ID
     *
     * @param parentId 父节点ID
     */
    void setParentId(ID parentId);
}
