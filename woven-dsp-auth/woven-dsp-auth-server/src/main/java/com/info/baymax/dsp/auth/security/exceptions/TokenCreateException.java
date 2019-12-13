package com.info.baymax.dsp.auth.security.exceptions;

/**
 * 说明：令牌创建异常. <br>
 * @author yjw@jusfoun.com
 * @date 2017年12月25日 上午11:20:02
 */
public class TokenCreateException extends TokenException {

	private static final long serialVersionUID = 2381145382073850927L;

	public TokenCreateException(String msg) {
		super(msg);
	}

	public TokenCreateException(String msg, Throwable t) {
		super(msg, t);
	}

}
