package com.jusfoun.services.auth.server.oauth2.kaptcha.filter;

import org.springframework.security.core.AuthenticationException;

/**
 * Kaptcha 认证异常
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-18 14:14:31
 */
public class KaptchaException extends AuthenticationException {
	private static final long serialVersionUID = -8477777932444230082L;

	public KaptchaException(String msg, Throwable t) {
		super(msg, t);
	}

	public KaptchaException(String msg) {
		super(msg);
	}

}
