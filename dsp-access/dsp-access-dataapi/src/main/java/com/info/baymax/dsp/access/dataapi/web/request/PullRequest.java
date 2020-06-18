package com.info.baymax.dsp.access.dataapi.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
public class PullRequest extends DataRequest {
    private static final long serialVersionUID = 945291461084587382L;

    @ApiModelProperty("服务ID")
    @NotNull
    private Long dataServiceId;

    @ApiModelProperty("数据偏移游标")
    @PositiveOrZero
    private int offset;

    @ApiModelProperty("请求数据量")
    @Positive
    private int size;
}
