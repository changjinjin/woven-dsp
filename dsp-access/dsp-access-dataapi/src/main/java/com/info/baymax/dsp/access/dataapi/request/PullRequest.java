package com.info.baymax.dsp.access.dataapi.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class PullRequest implements Serializable {
	private static final long serialVersionUID = 945291461084587382L;

	@ApiModelProperty("服务ID")
	private String dataServiceId;
	@ApiModelProperty("应用接入key")
	private String accessKey;
	@ApiModelProperty("数据偏移游标")
	private int offset;
	@ApiModelProperty("请求数据量")
	private int size;
}
