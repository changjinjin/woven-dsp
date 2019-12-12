package com.merce.woven.common.service.tree.sreach;

import com.merce.woven.common.service.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;

/**
 * 可数据搜索的树结构数据类型查询接口
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:58
 */
@ApiModel
public abstract class TreeSearchNode<T extends TreeSearchable<T>> extends TreeNode<T> implements TreeSearchable<T> {

    /**
     * 是否是叶子节点
     */
    @ApiModelProperty("是否是叶子节点")
    @Transient
    private boolean leaf;

    @Override
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }
}
