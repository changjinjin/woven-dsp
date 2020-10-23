package com.merce.woven.cas.client.reactive.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class CasSecurityConfig {
    @Autowired
    private ServerAuthenticationEntryPoint authenticationEntryPoint;
    // @Autowired
    // private ServerRequestCache requestCache;
    @Autowired
    private AuthenticationWebFilter casAuthenticationWebFilter;
    @Autowired
    private AuthorizationWebFilter casAuthorizationWebFilter;
    @Autowired
    private LogoutWebFilter logoutWebFilter;
    @Autowired
    private ServerSecurityContextRepository serverSecurityContextRepository;
    @Autowired
    private CasServiceProperties casProperties;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http //
            .csrf().disable().cors()//
            .and()//
            .headers().frameOptions().disable()//
            .and()//
            .requestCache().disable()// requestCache(this.requestCache)//
            // .and()//
            .authorizeExchange()//
            .pathMatchers(casProperties.getWhiteList()).permitAll()//
            .anyExchange().authenticated()//
            .and()//
            .securityContextRepository(serverSecurityContextRepository)//
            .httpBasic().authenticationEntryPoint(this.authenticationEntryPoint) //
            .and()//
            .addFilterAt(casAuthenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)//
            .addFilterAt(casAuthorizationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)//
            .addFilterAt(logoutWebFilter, SecurityWebFiltersOrder.LOGOUT)//
            .build();
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
