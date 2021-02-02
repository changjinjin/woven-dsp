package com.info.baymax.common.service.tree;

import java.util.List;

import javax.persistence.Transient;

/**
 * 树结构数据检索查询接口
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:55
 */
public interface Treeable<T extends Treeable<T>> {

    /**
     * 获取子节点集合
     *
     * @return 子节点集合
     */
    List<T> getChildren();

    /**
     * 设置子节点集合
     *
     * @param children 子节点集合
     */
    void setChildren(List<T> children);

    /**
     * 是否存在子节点
     *
     * @return true-有子节点，false-无子节点
     */
    @Transient
    default boolean isHasChildren() {
        List<T> children = getChildren();
        return children != null && children.size() > 0;
    }
}
