package com.merce.woven.dsp.auth.server.oauth2.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * 自定义异常相应处理转化器
 * 
 * @author yjw@jusfoun.com
 * @date 2019年1月27日 下午5:02:03
 */
@Component
public class CustomWebResponseExceptionTranslator implements WebResponseExceptionTranslator<OAuth2Exception> {

	@Override
	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {

		OAuth2Exception oAuth2Exception = (OAuth2Exception) e;
		return ResponseEntity.status(oAuth2Exception.getHttpErrorCode())
				.body(new CustomOauth2Exception(oAuth2Exception.getMessage()));
	}
}
