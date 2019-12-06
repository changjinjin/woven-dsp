package com.merce.woven.common.entity.field.convertor;

public class ValueConvertorException extends RuntimeException {
    static final long serialVersionUID = -7034897190745766939L;

    public ValueConvertorException() {
        super();
    }

    public ValueConvertorException(String message) {
        super(message);
    }

    public ValueConvertorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValueConvertorException(Throwable cause) {
        super(cause);
    }

    protected ValueConvertorException(String message, Throwable cause, boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
