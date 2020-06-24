package com.info.baymax.dsp.access.dataapi.data;

public class DataReadException extends RuntimeException {
    private static final long serialVersionUID = -4206801624903838752L;

    public DataReadException() {
        super();
    }

    public DataReadException(String message) {
        super(message);
    }

    public DataReadException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataReadException(Throwable cause) {
        super(cause);
    }

    protected DataReadException(String message, Throwable cause, boolean enableSuppression,
                                boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
