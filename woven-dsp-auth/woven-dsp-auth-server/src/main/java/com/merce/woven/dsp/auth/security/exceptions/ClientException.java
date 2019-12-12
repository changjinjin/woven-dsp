package com.merce.woven.dsp.auth.security.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * 说明： client异常. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2017年11月14日 下午1:45:42
 */
public class ClientException extends AuthenticationException {
	private static final long serialVersionUID = -2711776825981689462L;

	private String clientId;

	public ClientException(String msg) {
		super(msg);
	}

	public ClientException(String msg, Throwable t) {
		super(msg, t);
	}

	public ClientException(String msg, String clientId) {
		this(msg);
		this.clientId = clientId;
	}

	public ClientException(String msg, String clientId, Throwable t) {
		super(msg, t);
		this.clientId = clientId;
	}

	public String getClientId() {
		return clientId;
	}
}
