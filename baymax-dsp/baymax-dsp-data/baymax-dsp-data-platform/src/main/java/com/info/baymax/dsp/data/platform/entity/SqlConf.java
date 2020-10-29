package com.info.baymax.dsp.data.platform.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class SqlConf {

    @ApiModelProperty("sql模板")
    private String sqlTemplate;
    @ApiModelProperty("参数列表")
    private List<Parameter> parameters;
}

@Data
class Parameter {

    @ApiModelProperty("参数名称")
    private String name;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("注释")
    private String content;
}
