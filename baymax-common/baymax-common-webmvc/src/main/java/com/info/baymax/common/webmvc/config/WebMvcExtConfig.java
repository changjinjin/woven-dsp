package com.info.baymax.common.webmvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.info.baymax.common.config.JacksonConfig.JacksonExtProperties;
import com.info.baymax.common.webmvc.servlet.result.PathTweakingRequestMappingHandlerMapping;
import com.info.baymax.common.webmvc.servlet.result.ServletFilterFieldsHandlerResultHandler;
import com.info.baymax.common.webmvc.servlet.result.ServletMappingJackson2HttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
public class WebMvcExtConfig implements WebMvcConfigurer {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JacksonExtProperties properties;

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

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }

    @Configuration
    class WebMvcRegistrationsConfig implements WebMvcRegistrations {
        @Override
        public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
            return new PathTweakingRequestMappingHandlerMapping();
        }
    }
}
