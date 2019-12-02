package com.merce.woven.dsp.auth.server.oauth2.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.merce.woven.common.message.result.Response;

/**
 * 说明：自定义异常响应逻辑. <br>
 * 
 * @author yjw@jusfoun.com
 * @date 2017年11月3日 上午10:12:05
 */
public class CustomAuthenticationExceptionEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException authException) throws IOException, ServletException {
		CustomOauth2ExceptionHandler handler = new CustomOauth2ExceptionHandler();
		Response<?> result = handler.handle(authException);
		// 设置response属性
		response.setStatus(result.getStatus());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		// 向客户端写入消息体
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(response.getWriter(), result);
	}

}