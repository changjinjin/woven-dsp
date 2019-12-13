package com.info.baymax.dsp.auth.api.exception;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

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
		gen.writeNumberField("status", value.getHttpErrorCode());
		gen.writeStringField("message", StringUtils.defaultString(value.getSummary(), value.getMessage()));
		gen.writeStringField("path", request.getServletPath());
		gen.writeStringField("timestamp", String.valueOf(new Date().getTime()));
		if (value.getAdditionalInformation() != null) {
			for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
				String key = entry.getKey();
				String add = entry.getValue();
				gen.writeStringField(key, add);
			}
		}
		gen.writeEndObject();
	}
}
