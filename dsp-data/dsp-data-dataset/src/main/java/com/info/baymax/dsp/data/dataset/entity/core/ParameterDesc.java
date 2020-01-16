package com.info.baymax.dsp.data.dataset.entity.core;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@JsonPropertyOrder({"name", "category", "refs", "defaultVal","value", "description"})
public class ParameterDesc implements Serializable{
	private static final long serialVersionUID = -4526446217010424621L;

	@ApiModelProperty("参数名称")
    private String name = "";
	@ApiModelProperty("参数类别")
    private String category = "ref"; // var or ref
	@ApiModelProperty("引用列表")
    private List<String> refs = new ArrayList<String>();
	@ApiModelProperty("默认值")
    private String defaultVal = "";
	@ApiModelProperty("值列表")
    private List<String> value = null;
	@ApiModelProperty("描述")
    private String description = "'";

    public ParameterDesc() {
    }

    public ParameterDesc(String name, String category, List<String> refs, String defaultVal, String description) {
        this.name = name;
        this.category = category;
        this.refs = refs;
        this.defaultVal = defaultVal;
        this.description = description;
    }

    public ParameterDesc(String name, String category, List<String> refs, String defaultVal, String options, boolean disabled, String dependOn, String description) {
        this.name = name;
        this.category = category;
        this.refs = refs;
        this.defaultVal = defaultVal;
        this.description = description;
        if (options != null) {
            this.description = this.description + ";options?" + options;
            if(options.split(",").length>1){
                this.value = Arrays.asList(options.split(","));
            }
        }

        if (disabled) {
            this.description = this.description + ";disabled?true";
        }

        if (dependOn != null) {
            this.description = this.description + ";dependon?" + dependOn;
        }
    }
}
