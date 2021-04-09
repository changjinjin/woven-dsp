package com.info.baymax.common.webmvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.config.JacksonConfig.JacksonExtSerializationProperties;
import com.info.baymax.common.webmvc.servlet.result.PathTweakingRequestMappingHandlerMapping;
import com.info.baymax.common.webmvc.servlet.result.ServletFilterFieldsHandlerResultHandler;
import com.info.baymax.common.webmvc.servlet.result.ServletMappingJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
public class WebMvcExtConfig implements WebMvcConfigurer {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private JacksonExtSerializationProperties properties;

	@Bean
	public ServletListenerRegistrationBean<RequestContextListener> servletListenerRegistrationBean() {
		return new ServletListenerRegistrationBean<RequestContextListener>(new RequestContextListener());
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		WebMvcConfigurer.super.configureMessageConverters(converters);
		converters.add(new ServletMappingJackson2HttpMessageConverter(properties));
	}

	@Override
	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
		WebMvcConfigurer.super.addReturnValueHandlers(handlers);
		handlers.add(new ServletFilterFieldsHandlerResultHandler(objectMapper));
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// 设置允许跨域的路由
		registry.addMapping("/**")
			// 是否允许证书（cookies）
			.allowCredentials(true)
			// 设置允许跨域请求的域名
			.allowedOriginPatterns("*")
			// 设置允许的方法
			.allowedMethods("*")
			// 设置header
			.allowedHeaders("*")
			// 跨域允许时间
			.maxAge(3600);
	}

	@Configuration
	class WebMvcRegistrationsConfig implements WebMvcRegistrations {
		@Override
		public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
			return new PathTweakingRequestMappingHandlerMapping();
		}
	}
}
