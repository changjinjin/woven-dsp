package com.info.baymax.common.queryapi.field;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public abstract class CriteriaItem implements Comparable<CriteriaItem>, Serializable {
    private static final long serialVersionUID = 6417720184834620402L;

    // 条件排序序号
    @ApiModelProperty(value = "条件排序序号")
    protected int index;

    // 是否是条件组
    @ApiModelProperty(value = "是否是条件组",hidden = true)
    private boolean group;

    @Override
    public int compareTo(CriteriaItem o) {
        if (o == null)
            return 1;
        return index - o.getIndex();
    }
}
