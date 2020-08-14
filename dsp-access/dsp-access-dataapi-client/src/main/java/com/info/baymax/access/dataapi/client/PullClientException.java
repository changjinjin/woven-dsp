package com.info.baymax.access.dataapi.client;

public class PullClientException extends Exception {
    static final long serialVersionUID = -7034897190745766939L;

    public PullClientException() {
        super();
    }

    public PullClientException(String message) {
        super(message);
    }

    public PullClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public PullClientException(Throwable cause) {
        super(cause);
    }

    protected PullClientException(String message, Throwable cause, boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
