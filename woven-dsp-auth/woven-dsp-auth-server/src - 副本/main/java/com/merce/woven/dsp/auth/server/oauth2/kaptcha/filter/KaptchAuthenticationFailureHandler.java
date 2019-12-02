package com.jusfoun.services.auth.server.oauth2.kaptcha.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jusfoun.common.message.result.BaseResponse;
import com.jusfoun.common.message.result.ErrType;

/**
 * Kaptch 验证异常处理器
 * 
 * @author yjw@jusfoun.com
 * @date 2019-02-21 09:30:24
 */
public class KaptchAuthenticationFailureHandler implements AuthenticationFailureHandler {
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		if (exception instanceof KaptchaException) {
			ObjectMapper objectMapper = new ObjectMapper();
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			objectMapper.writeValue(response.getWriter(), BaseResponse.exception(ErrType.UNAUTHORIZED, "验证码错误"));
		}
	}
}
