package com.merce.woven.dsp.auth.server.oauth2.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 自定义Oauth2异常
 * 
 * @author yjw@jusfoun.com
 * @date 2019年1月27日 下午4:49:05
 */
@JsonSerialize(using = CustomOauth2ExceptionSerializer.class)
public class CustomOauth2Exception extends OAuth2Exception {
	private static final long serialVersionUID = -8871557007209001038L;

	public CustomOauth2Exception(String msg) {
		super(msg);
	}
}
