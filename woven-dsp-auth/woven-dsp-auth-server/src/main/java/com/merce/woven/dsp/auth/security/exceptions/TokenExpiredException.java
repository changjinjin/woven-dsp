package com.merce.woven.dsp.auth.security.exceptions;

/**
 * 说明： 令牌过期异常. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2017年11月9日 下午7:45:02
 */
public class TokenExpiredException extends TokenException {

	private static final long serialVersionUID = -5959543783324224864L;

	public TokenExpiredException(String msg) {
		super(msg);
	}

	public TokenExpiredException(String msg, Throwable t) {
		super(msg, t);
	}

	public TokenExpiredException(String msg, String token) {
		super(msg, token);
	}

	public TokenExpiredException(String msg, String token, Throwable t) {
		super(msg, token, t);
	}

}