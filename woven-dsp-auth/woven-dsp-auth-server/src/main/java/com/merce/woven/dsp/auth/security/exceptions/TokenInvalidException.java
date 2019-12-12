package com.merce.woven.dsp.auth.security.exceptions;

/**
 * 说明：Token失效. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2017年11月29日 上午10:35:04
 */
public class TokenInvalidException extends TokenException {
	private static final long serialVersionUID = -294671188037098603L;

	public TokenInvalidException(String msg) {
		super(msg);
	}

	public TokenInvalidException(String msg, Throwable t) {
		super(msg, t);
	}

	public TokenInvalidException(String msg, String token) {
		super(msg, token);
	}

	public TokenInvalidException(String msg, String token, Throwable t) {
		super(msg, token, t);
	}

}
