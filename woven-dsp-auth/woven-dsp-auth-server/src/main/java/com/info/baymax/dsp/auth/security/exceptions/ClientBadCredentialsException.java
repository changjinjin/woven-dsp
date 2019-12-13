package com.info.baymax.dsp.auth.security.exceptions;

/**
 * client密码错误
 * @author jingwei.yang
 * @date 2019年6月18日 上午10:34:12
 */
public class ClientBadCredentialsException extends ClientException {
	private static final long serialVersionUID = -2711776825981689462L;

	public ClientBadCredentialsException(String msg) {
		super(msg);
	}

	public ClientBadCredentialsException(String msg, Throwable t) {
		super(msg, t);
	}

	public ClientBadCredentialsException(String msg, String clientId) {
		super(msg, clientId);
	}

	public ClientBadCredentialsException(String msg, String clientId, Throwable t) {
		super(msg, clientId, t);
	}
}
