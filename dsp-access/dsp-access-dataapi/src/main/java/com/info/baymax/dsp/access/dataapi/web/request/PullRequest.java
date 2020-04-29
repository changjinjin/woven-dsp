package com.info.baymax.dsp.access.dataapi.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class PullRequest extends DataRequest {
    private static final long serialVersionUID = 945291461084587382L;

    @ApiModelProperty("服务ID")
    private String dataServiceId;
    @ApiModelProperty("数据偏移游标")
    private int offset;
    @ApiModelProperty("请求数据量")
    private int size;
}
