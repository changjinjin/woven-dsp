package com.alibaba.csp.sentinel.dashboard.rule;

public class RuleException extends RuntimeException {
    private static final long serialVersionUID = 7618085151547002848L;

    public RuleException() {
        super();
    }

    public RuleException(String message) {
        super(message);
    }

    public RuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuleException(Throwable cause) {
        super(cause);
    }

    protected RuleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
