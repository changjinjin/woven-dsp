package com.info.baymax.dsp.auth.security.exceptions;

/**
 * 说明： clientId错误. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2017年11月14日 下午1:45:42
 */
public class ClientIdNotFoundException extends ClientException {
	private static final long serialVersionUID = -2711776825981689462L;

	public ClientIdNotFoundException(String msg) {
		super(msg);
	}

	public ClientIdNotFoundException(String msg, Throwable t) {
		super(msg, t);
	}

	public ClientIdNotFoundException(String msg, String clientId) {
		super(msg, clientId);
	}

	public ClientIdNotFoundException(String msg, String clientId, Throwable t) {
		super(msg, clientId, t);
	}
}
