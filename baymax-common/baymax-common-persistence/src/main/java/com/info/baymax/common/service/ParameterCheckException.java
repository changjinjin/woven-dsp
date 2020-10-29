package com.info.baymax.common.service;

/**
 * 参数检查异常
 *
 * @author jingwei.yang
 * @date 2019年9月4日 下午12:10:44
 */
public class ParameterCheckException extends RuntimeException {
    private static final long serialVersionUID = -1118261268706281985L;

    public ParameterCheckException() {
        super();
    }

    public ParameterCheckException(String message) {
        super(message);
    }

    public ParameterCheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterCheckException(Throwable cause) {
        super(cause);
    }

    protected ParameterCheckException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
