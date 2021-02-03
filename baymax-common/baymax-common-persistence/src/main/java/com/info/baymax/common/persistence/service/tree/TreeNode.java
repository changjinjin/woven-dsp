package com.info.baymax.common.persistence.service.tree;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Transient;
import java.util.List;

/**
 * 树结构数据检索查询接口
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:55
 */
@ApiModel
public abstract class TreeNode<T extends Treeable<T>> implements Treeable<T> {

    /**
     * 子节点集合
     */
    @ApiModelProperty("子节点集合")
    @Transient
    private List<T> children;

    @Override
    public List<T> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<T> children) {
        this.children = children;
    }

}
