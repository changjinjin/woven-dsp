//package com.merce.woven.dsp.auth.config;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.web.context.request.RequestContextListener;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
//import org.springframework.web.filter.CorsFilter;
//import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//
//	@Bean
//	public ServletListenerRegistrationBean<RequestContextListener> servletListenerRegistrationBean() {
//		return new ServletListenerRegistrationBean<RequestContextListener>(new RequestContextListener());
//	}
//
//	@Override
//	public void addResourceHandlers(ResourceHandlerRegistry registry) {
//		registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/",
//				"classpath:/resources/", "classpath:/public/");
//		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
//		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//	}
//
//	@Override
//	public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
//		WebMvcConfigurer.super.addReturnValueHandlers(handlers);
//	}
//
//	/**
//	 * 跨域
//	 */
//	@Bean
//	public FilterRegistrationBean<CorsFilter> corsFilter() {
//		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//		CorsConfiguration config = new CorsConfiguration();
//		config.setAllowCredentials(true);
//		config.addAllowedOrigin("*");
//		config.addAllowedHeader("*");
//		config.addAllowedMethod("*");
//		source.registerCorsConfiguration("/**", config);
//		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
//		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//		return bean;
//	}
//}
