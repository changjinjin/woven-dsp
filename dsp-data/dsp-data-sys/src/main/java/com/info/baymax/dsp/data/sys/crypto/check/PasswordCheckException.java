package com.info.baymax.dsp.data.sys.crypto.check;

public class PasswordCheckException extends RuntimeException {
	private static final long serialVersionUID = -8814860243412814598L;

	public PasswordCheckException() {
		super();
	}

	public PasswordCheckException(String message) {
		super(message);
	}

	public PasswordCheckException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordCheckException(Throwable cause) {
		super(cause);
	}

	protected PasswordCheckException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
