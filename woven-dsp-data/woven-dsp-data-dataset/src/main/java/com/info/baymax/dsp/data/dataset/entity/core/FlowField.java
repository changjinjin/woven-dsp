package com.info.baymax.dsp.data.dataset.entity.core;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: haijun
 * @Date: 2019/12/20 14:51
 */
@Data
public class FlowField implements Serializable {

    private static final long serialVersionUID = -2172741612149946870L;

    @ApiModelProperty("名称")
    private String column = "";

    @ApiModelProperty("类型")
    private String type = "";

    @ApiModelProperty("别名")
    private String alias = "";

    @ApiModelProperty("描述")
    private String description = "";

    public FlowField() {
    }

    public FlowField(String column, String type, String alias) {
        this.column = column;
        this.type = type;
        this.alias = alias;
    }

    public FlowField(String column, String type, String alias, String description) {
        this.column = column;
        this.type = type;
        this.alias = alias;
        this.description = description;
    }

    public FlowField(String column, String type) {
        this.column = column;
        this.type = type;
    }
}
