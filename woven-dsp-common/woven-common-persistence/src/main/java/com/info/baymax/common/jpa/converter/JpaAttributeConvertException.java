package com.info.baymax.common.jpa.converter;

import javax.persistence.PersistenceException;

public class JpaAttributeConvertException extends PersistenceException {
    private static final long serialVersionUID = 5813187987959846250L;

    public JpaAttributeConvertException() {
        super();
    }

    public JpaAttributeConvertException(String message) {
        super(message);
    }

    public JpaAttributeConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public JpaAttributeConvertException(Throwable cause) {
        super(cause);
    }

}
