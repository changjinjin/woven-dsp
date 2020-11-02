package com.info.baymax.common.queryapi.exception;

import com.info.baymax.common.queryapi.result.ErrMsg;

/**
 * Service 层异常
 *
 * @author: jingwei.yang
 * @date: 2019年4月23日 下午2:56:20
 */
public class ServiceException extends BizException {
    private static final long serialVersionUID = 1494875666602336146L;

    public ServiceException(ErrMsg errMsg) {
        super(errMsg);
    }

    public ServiceException(ErrMsg errMsg, Throwable cause) {
        super(errMsg, cause);
    }

    public ServiceException(ErrMsg errMsg, String customMessage) {
        super(errMsg, customMessage);
    }

    public ServiceException(ErrMsg errMsg, String customMessage, Throwable cause) {
        super(errMsg, customMessage, cause);
    }

    public ServiceException(ErrMsg errMsg, String customMessage, Object[] args) {
        super(errMsg, customMessage, args);
    }

    public ServiceException(ErrMsg errMsg, Object[] args) {
        super(errMsg, args);
    }

    public ServiceException(ErrMsg errMsg, String customMessage, Object[] args, Throwable cause) {
        super(errMsg, customMessage, args, cause);
    }
}
