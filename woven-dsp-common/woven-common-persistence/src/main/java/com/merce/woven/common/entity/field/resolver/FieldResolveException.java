package com.merce.woven.common.entity.field.resolver;

public class FieldResolveException extends RuntimeException {
    private static final long serialVersionUID = -3461345203529071193L;

    public FieldResolveException() {
        super();
    }

    public FieldResolveException(String message) {
        super(message);
    }

    public FieldResolveException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldResolveException(Throwable cause) {
        super(cause);
    }

    protected FieldResolveException(String message, Throwable cause, boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
