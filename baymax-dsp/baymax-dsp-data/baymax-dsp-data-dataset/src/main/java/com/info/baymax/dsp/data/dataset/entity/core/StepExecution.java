package com.info.baymax.dsp.data.dataset.entity.core;

import com.info.baymax.dsp.data.dataset.entity.Status;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class StepExecution implements Serializable {
	private static final long serialVersionUID = 913921495645906093L;

	@ApiModelProperty("主键")
	private String id;
	@ApiModelProperty("名称")
	private String name;
	@ApiModelProperty("状态信息")
	private Status status;
}
