package com.info.baymax.common.service.tree.sreach;

import com.info.baymax.common.service.tree.Treeable;

/**
 * 可数据搜索的树结构数据类型接口
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:55
 */
public interface TreeSearchable<T extends TreeSearchable<T>> extends Treeable<T> {

    /**
     * 是否是叶子节点
     *
     * @return 是否是叶子节点
     */
    boolean isLeaf();

    /**
     * 设置是否是叶子节点
     */
    void setLeaf(boolean leaf);

    /**
     * 获取匹配的字段值集合
     *
     * @return 匹配
     */
    String[] matchFeilds();
}
