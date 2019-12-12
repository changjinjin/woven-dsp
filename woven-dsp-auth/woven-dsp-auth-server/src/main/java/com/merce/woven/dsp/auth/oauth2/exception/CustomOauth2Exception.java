package com.merce.woven.dsp.auth.oauth2.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = CustomOauth2ExceptionSerializer.class)
public class CustomOauth2Exception extends OAuth2Exception {
	private static final long serialVersionUID = -8871557007209001038L;

	public CustomOauth2Exception(String msg) {
		super(msg);
	}
}
