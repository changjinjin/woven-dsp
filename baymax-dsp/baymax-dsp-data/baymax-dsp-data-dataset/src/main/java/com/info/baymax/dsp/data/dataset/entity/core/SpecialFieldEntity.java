package com.info.baymax.dsp.data.dataset.entity.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: haijun
 * @Date: 2020/2/18 0018 17:13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class SpecialFieldEntity implements Serializable {
    private static final long serialVersionUID = 7550075583443211682L;

    @ApiModelProperty("字段名称")
    private String name;

    @ApiModelProperty("字段类型")
    private String type;

    @ApiModelProperty("别名")
    private String alias;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("当前值")
    private String currVal;
}
