package com.merce.woven.common.base.tree.id;

import com.merce.woven.common.base.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 有主键的树结构数据类型抽象实现
 *
 * @author jingwei.yang
 * @date 2019-05-29 15:58
 */
@ApiModel
public abstract class TreeIdNode<ID extends Serializable, T extends TreeIdable<ID, T>> extends TreeNode<T>
    implements TreeIdable<ID, T> {

    @ApiModelProperty("ID")
    protected ID id;

    @ApiModelProperty("父节点ID")
    protected ID parentId;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public ID getParentId() {
        return parentId;
    }

    @Override
    public void setParentId(ID parentId) {
        this.parentId = parentId;
    }

}
