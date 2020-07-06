package com.info.baymax.common.message.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
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
     * 业务报文
     */
    @ApiModelProperty("业务报文主体：当接口只需要返回简单状态时该节点为空")
    @JsonInclude(value = Include.NON_NULL)
    protected T content;

    protected AbstractResponse(ErrMsg errMsg, T content) {
        this(errMsg.getStatus(), errMsg.getMessage(), content);
    }

    protected AbstractResponse(Integer status, String message, T content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }

    public boolean isOk() {
        return this.status != null && this.status.intValue() == 0;
    }
}
