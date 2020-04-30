package com.info.baymax.dsp.common.webflux.config;

import com.info.baymax.dsp.common.webflux.server.error.GlobalErrorAttributes;
import com.info.baymax.dsp.common.webflux.server.error.GlobalErrorWebExceptionHandler;
import com.info.baymax.dsp.common.webflux.server.result.ServerFilterFieldsHandlerResultHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.ReactiveAdapterRegistry;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.reactive.accept.RequestedContentTypeResolver;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.server.i18n.AcceptHeaderLocaleContextResolver;
import org.springframework.web.server.i18n.LocaleContextResolver;
import reactor.core.publisher.Mono;

import java.util.Locale;
import java.util.stream.Collectors;

@Configuration
public class WebFluxExtConfig implements WebFluxConfigurer {

    @Autowired
    private ServerProperties serverProperties;

    @Autowired
    private RequestedContentTypeResolver webFluxContentTypeResolver;

    @Autowired
    private ReactiveAdapterRegistry webFluxAdapterRegistry;

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ResourceProperties resourceProperties;

    @Autowired
    private ObjectProvider<ViewResolver> viewResolversProvider;

    @Bean
    protected LocaleContextResolver createLocaleContextResolver() {
        LocaleContextHolder.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);
        AcceptHeaderLocaleContextResolver acceptHeaderLocaleContextResolver = new AcceptHeaderLocaleContextResolver();
        acceptHeaderLocaleContextResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return acceptHeaderLocaleContextResolver;
    }

    @Bean
    public ServerFilterFieldsHandlerResultHandler serverFilterFieldsHandlerResultHandler() {
        return new ServerFilterFieldsHandlerResultHandler(serverCodecConfigurer.getWriters(),
            webFluxContentTypeResolver, webFluxAdapterRegistry);
    }

    // @Bean
    // @Order(-2)
    public GlobalErrorWebExceptionHandler globalErrorWebExceptionHandler(ErrorAttributes errorAttributes) {
        GlobalErrorWebExceptionHandler exceptionHandler = new GlobalErrorWebExceptionHandler(errorAttributes,
            resourceProperties, applicationContext);
        exceptionHandler.setViewResolvers(viewResolversProvider.orderedStream().collect(Collectors.toList()));
        exceptionHandler.setMessageWriters(this.serverCodecConfigurer.getWriters());
        exceptionHandler.setMessageReaders(this.serverCodecConfigurer.getReaders());
        return exceptionHandler;
    }

    @Bean
    @Primary
    public GlobalErrorAttributes errorAttributes() {
        return new GlobalErrorAttributes(this.serverProperties.getError().isIncludeException());
    }

    // 这里为支持的请求头，如果有自定义的header字段请自己添加（不知道为什么不能使用*）
    private static final String ALLOWED_HEADERS = "x-requested-with, authorization, Content-Type,credential, Authorization,X-AUTH-TOKEN, X-XSRF-TOKEN,token,username,client";
    private static final String ALLOWED_METHODS = "*";
    private static final String ALLOWED_ORIGIN = "*";
    private static final String ALLOWED_Expose = "*";
    private static final String MAX_AGE = "18000L";

    @Bean
    @Order(Integer.MIN_VALUE + 1)
    public WebFilter corsFilter() {
        return (ServerWebExchange ctx, WebFilterChain chain) -> {
            ServerHttpRequest request = ctx.getRequest();
            if (CorsUtils.isCorsRequest(request)) {
                ServerHttpResponse response = ctx.getResponse();
                HttpHeaders headers = response.getHeaders();
                headers.add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
                headers.add("Access-Control-Allow-Methods", ALLOWED_METHODS);
                headers.add("Access-Control-Max-Age", MAX_AGE);
                headers.add("Access-Control-Allow-Headers", ALLOWED_HEADERS);
                headers.add("Access-Control-Expose-Headers", ALLOWED_Expose);
                headers.add("Access-Control-Allow-Credentials", "true");
                if (request.getMethod() == HttpMethod.OPTIONS) {
                    response.setStatusCode(HttpStatus.OK);
                    return Mono.empty();
                }
            }
            return chain.filter(ctx);
        };
    }

}
