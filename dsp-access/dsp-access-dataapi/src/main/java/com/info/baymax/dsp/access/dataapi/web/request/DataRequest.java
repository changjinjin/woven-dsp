package com.info.baymax.dsp.access.dataapi.web.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.io.Serializable;

/**
 * 数据请求的公共参数
 *
 * @author jingwei.yang
 * @date 2020年4月20日 上午11:28:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class DataRequest implements Serializable {
    private static final long serialVersionUID = 1837415801984192215L;

    @ApiModelProperty("消费者应用accessKey")
    @NotBlank
    private String accessKey;

    @ApiModelProperty("接口请求的时间戳")
    @Positive
    private long timestamp;

    @ApiModelProperty("是否使用加密，如果使用这返回报文需要加密处理")
    private boolean encrypted;
}
