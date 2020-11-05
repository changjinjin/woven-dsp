package com.info.baymax.common.queryapi.exception;

import com.info.baymax.common.queryapi.result.ErrMsg;

/**
 * Controller层异常
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:55:00
 */
public class ControllerException extends BizException {
    private static final long serialVersionUID = -7428495813034319173L;

    public ControllerException(ErrMsg errMsg) {
        super(errMsg);
    }

    public ControllerException(ErrMsg errMsg, Throwable cause) {
        super(errMsg, cause);
    }

    public ControllerException(ErrMsg errMsg, String customMessage) {
        super(errMsg, customMessage);
    }

    public ControllerException(ErrMsg errMsg, String customMessage, Throwable cause) {
        super(errMsg, customMessage, cause);
    }

    protected ControllerException(ErrMsg errMsg, String customMessage, Object[] args) {
        super(errMsg, customMessage, args);
    }

    public ControllerException(ErrMsg errMsg, Object[] args) {
        super(errMsg, args);
    }

    protected ControllerException(ErrMsg errMsg, String customMessage, Object[] args, Throwable cause) {
        super(errMsg, customMessage, args, cause);
    }
}
