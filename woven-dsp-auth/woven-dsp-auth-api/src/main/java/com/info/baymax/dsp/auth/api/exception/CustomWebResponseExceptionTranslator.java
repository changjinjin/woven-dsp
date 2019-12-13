package com.info.baymax.dsp.auth.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;

public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
		if (e instanceof AuthenticationException) {
			if (e instanceof OAuth2Exception) {
				OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
				return ResponseEntity.status(oAuth2Exception.getHttpErrorCode()).body(oAuth2Exception);
			} else if (e instanceof AuthenticationServiceException) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new CustomOauth2Exception(
						OAuth2Exception.ACCESS_DENIED, HttpStatus.FORBIDDEN.value(), null, e.getMessage()));
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CustomOauth2Exception(
						OAuth2Exception.INVALID_GRANT, HttpStatus.UNAUTHORIZED.value(), null, e.getMessage()));
			}
		} else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
					OAuth2Exception.create(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), e.getMessage()));
	}
}
