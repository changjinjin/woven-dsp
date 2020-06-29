package com.info.baymax.dsp.access.dataapi.web.request;

import com.info.baymax.dsp.access.dataapi.data.Query;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel
@NoArgsConstructor
public class PullRequest extends DataRequest {
    private static final long serialVersionUID = 945291461084587382L;

    @ApiModelProperty("服务ID")
    @NotNull
    private Long dataServiceId;

    @ApiModelProperty("查询条件")
    @NotNull
    private Query query;

    public PullRequest(Long dataServiceId, Query query, String accessKey, long timestamp, boolean encrypted) {
        super(accessKey, timestamp, encrypted);
        this.dataServiceId = dataServiceId;
        this.query = query;
    }
}
