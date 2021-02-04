package com.info.baymax.common.core.exception;

import com.info.baymax.common.core.result.ErrMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 定义业务异常
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:55:39
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BizException extends RuntimeException {
    private static final long serialVersionUID = -6187335538554881851L;

    /**
     * 错误类型
     */
    private ErrMsg errMsg;

    /**
     * 提示参数列表
     */
    private Object[] args;

    /**
     * 自定义异常信息
     */
    private String customMessage;

    public BizException(ErrMsg errMsg) {
        this(errMsg, "");
    }

    public BizException(ErrMsg errMsg, Throwable cause) {
        super(errMsg.getMessage(), cause);
        this.errMsg = errMsg;
    }

    public BizException(ErrMsg errMsg, String customMessage) {
        super(errMsg.getMessage());
        this.errMsg = errMsg;
        this.customMessage = customMessage;
    }

    public BizException(ErrMsg errMsg, String customMessage, Throwable cause) {
        this(errMsg, cause);
        this.errMsg = errMsg;
        this.customMessage = customMessage;
    }

    public BizException(ErrMsg errMsg, Object[] args) {
        super(errMsg.getMessage());
        this.errMsg = errMsg;
        this.args = args;
    }

    public BizException(ErrMsg errMsg, String customMessage, Object[] args) {
        super(errMsg.getMessage());
        this.errMsg = errMsg;
        this.args = args;
        this.customMessage = customMessage;
    }

    public BizException(ErrMsg errMsg, String customMessage, Object[] args, Throwable cause) {
        this(errMsg, cause);
        this.args = args;
        this.customMessage = customMessage;
    }

}
