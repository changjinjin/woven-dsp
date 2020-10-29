package com.info.baymax.common.service.tree.id;

import com.info.baymax.common.service.tree.TreeNode;
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
    private static final long serialVersionUID = 1079089709388643511L;

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
