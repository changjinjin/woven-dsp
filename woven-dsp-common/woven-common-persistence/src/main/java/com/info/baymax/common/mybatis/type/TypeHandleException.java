package com.info.baymax.common.mybatis.type;

public class TypeHandleException extends RuntimeException {
    private static final long serialVersionUID = -4600260639693018759L;

    public TypeHandleException() {
        super();
    }

    public TypeHandleException(String message) {
        super(message);
    }

    public TypeHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public TypeHandleException(Throwable cause) {
        super(cause);
    }

}
