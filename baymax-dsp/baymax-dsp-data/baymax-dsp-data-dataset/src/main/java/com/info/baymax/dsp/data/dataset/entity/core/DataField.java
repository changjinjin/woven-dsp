package com.info.baymax.dsp.data.dataset.entity.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataField implements Serializable {
    private static final long serialVersionUID = 4042896789682591803L;

    @ApiModelProperty("名称")
    private String name = "";

    @ApiModelProperty("类型")
    private String type = "";

    @ApiModelProperty("别名")
    private String alias = "";

    @ApiModelProperty("备注")
    private String description = "";

    public DataField() {
    }

    public DataField(String name, String type, String alias) {
        this.name = name;
        this.type = type;
        this.alias = alias;
    }

    public DataField(String name, String type, String alias, String description) {
        this.name = name;
        this.type = type;
        this.alias = alias;
        this.description = description;
    }

    public DataField(String name, String type) {
        this.name = name;
        this.type = type;
    }

}
