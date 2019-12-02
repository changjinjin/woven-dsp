package com.merce.woven.dsp.auth.server.oauth2.exception;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * 自定义异常信息序列化器
 * 
 * @author yjw@jusfoun.com
 * @date 2019年1月27日 下午4:51:38
 */
public class CustomOauth2ExceptionSerializer extends StdSerializer<CustomOauth2Exception> {
	private static final long serialVersionUID = 5017357288059867291L;

	public CustomOauth2ExceptionSerializer() {
		super(CustomOauth2Exception.class);
	}

	@Override
	public void serialize(CustomOauth2Exception value, JsonGenerator gen, SerializerProvider provider)
			throws IOException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		gen.writeStartObject();
		gen.writeNumberField("code", value.getHttpErrorCode());
		gen.writeStringField("message", value.getMessage());
		gen.writeStringField("path", request.getServletPath());
		gen.writeStringField("timestamp", String.valueOf(new Date().getTime()));
		gen.writeEndObject();
	}
}
