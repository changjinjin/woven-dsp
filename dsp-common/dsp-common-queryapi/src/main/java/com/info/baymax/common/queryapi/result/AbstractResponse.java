package com.info.baymax.common.queryapi.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@ApiModel
@Getter
@ToString
public abstract class AbstractResponse<T> implements ErrMsg, Serializable {
    private static final long serialVersionUID = -5597707689816894840L;

    /**
     * 业务状态码
     */
    @ApiModelProperty("业务状态码：返回0表示请求处理成功，否则表示处理失败不是预期结果")
    protected Integer status;

    /**
     * 业务错误信息
     */
    @ApiModelProperty("业务错误信息：status字段为非0值时返回对应的错误信息")
    protected String message;
    
    /**
     *  详情描述，错误信息等
     */
    @ApiModelProperty("详情描述，错误信息等")
    protected Object details;

    /**
     * 业务报文
     */
    @ApiModelProperty("业务报文主体：当接口只需要返回简单状态时该节点为空")
    protected T content;

    protected AbstractResponse(ErrMsg errMsg, T content) {
        this(errMsg.getStatus(), errMsg.getMessage(), content,null);
    }

    protected AbstractResponse(Integer status, String message, T content,Object details) {
        this.status = status;
        this.message = message;
        this.content = content;
        this.details = details;
    }

    public boolean isOk() {
        return this.status != null && this.status.intValue() == 0;
    }
}
