package com.info.baymax.dsp.data.dataset.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ConfigItem {
	@ApiModelProperty("名称")
    private String name;
	@ApiModelProperty("值")
    private Object value;
    @ApiModelProperty("输入")
    private Object input;

    public ConfigItem() {
        this("", null);
    }

    public ConfigItem(String name, Object value) {
        this.name = name;
        this.value = value;
        this.input = value;
    }


}
