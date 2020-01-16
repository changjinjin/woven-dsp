package com.info.baymax.dsp.data.dataset.entity.core;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@JsonPropertyOrder({"name", "source", "sourceOutput", "target", "targetInput","input"})
public class LinkDesc implements Serializable {
	private static final long serialVersionUID = 1202030791878027510L;

	@ApiModelProperty("名称")
	private String name = "";
	@ApiModelProperty("来源节点")
    private String source = "";
	@ApiModelProperty("来源类型")
    private String sourceOutput = "output";
	@ApiModelProperty("目录类型")
    private String targetInput = "input";
	@ApiModelProperty("目标节点")
    private String target = "";

	@ApiModelProperty("")
    /**该字段为前端step连线输入使用**/
    private String input = "input";

    public LinkDesc() {
    }

    public LinkDesc(String name, String source, String sourceOutput, String targetInput, String target) {
        super();
        this.name = name;
        this.source = source;
        this.sourceOutput = sourceOutput;
        this.targetInput = targetInput;
        this.target = target;
        switch (targetInput) {
            case "input":
            case "right":
            case "left":
            case "input1":
            case "input2":
                this.input = targetInput;
                break;
            default:
                break;
        }
    }
}
