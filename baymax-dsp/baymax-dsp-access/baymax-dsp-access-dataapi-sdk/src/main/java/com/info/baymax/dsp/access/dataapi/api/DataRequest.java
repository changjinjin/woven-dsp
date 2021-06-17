package com.info.baymax.dsp.access.dataapi.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 数据请求的公共参数
 *
 * @author jingwei.yang
 * @date 2020年4月20日 上午11:28:30
 */
@ApiModel
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRequest<T> implements Serializable {
    public static final long serialVersionUID = 1837415801984192215L;

    @ApiModelProperty(value = "消费者应用accessKey", required = true)
    //@NotBlank
    protected String accessKey;

    @ApiModelProperty(value = "服务ID", required = true)
    @NotNull
    protected Long dataServiceId;

    @ApiModelProperty(value = "接口请求的时间戳", required = false)
    @Positive
    protected long timestamp;

    @ApiModelProperty(value = "是否使用加密，如果使用这返回报文需要加密处理", required = false)
    protected boolean encrypted;

    @ApiModelProperty(value = "查询条件", required = true)
    @NotNull
    protected T query;
}
