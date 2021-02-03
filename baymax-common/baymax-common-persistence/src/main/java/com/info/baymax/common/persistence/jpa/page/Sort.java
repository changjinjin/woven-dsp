package com.info.baymax.common.persistence.jpa.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@ApiModel
@Deprecated
public class Sort implements Serializable {
    private static final long serialVersionUID = -121408492162144003L;

    @ApiModelProperty("排序")
    private boolean sorted;
    @ApiModelProperty("不排序")
    private boolean unsorted;
    @ApiModelProperty("是否为空值")
    private boolean empty;
}
