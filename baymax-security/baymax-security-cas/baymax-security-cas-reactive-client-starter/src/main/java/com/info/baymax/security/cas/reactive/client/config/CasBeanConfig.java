package com.info.baymax.security.cas.reactive.client.config;

import cn.hutool.json.JSONObject;
import com.info.baymax.security.cas.reactive.authentication.CasReactiveAuthenticationManager;
import com.info.baymax.security.cas.reactive.client.userdetails.CasSamlUserDetailsService;
import com.info.baymax.security.cas.reactive.client.util.CASAuthUtil;
import com.info.baymax.security.cas.reactive.client.web.authentication.CASAuthConverter;
import com.info.baymax.security.cas.reactive.client.web.authentication.CASAuthEntryPoint;
import com.info.baymax.security.cas.reactive.client.web.authentication.CasRedirectStrategy;
import com.info.baymax.security.cas.reactive.client.web.authentication.validation.Saml11TicketValidator;
import com.info.baymax.security.cas.reactive.client.web.authorization.CasAuthorizationDecisionHandler;
import com.info.baymax.security.cas.reactive.client.web.authorization.CasAuthorizationManager;
import com.info.baymax.security.cas.reactive.web.CasServerAuthenticationConverter;
import com.info.baymax.security.cas.reactive.web.CasServerAuthenticationSuccessHandler;
import org.jasig.cas.client.validation.TicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.ServerRedirectStrategy;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.authentication.logout.LogoutWebFilter;
import org.springframework.security.web.server.authentication.logout.RedirectServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.security.web.server.savedrequest.ServerRequestCache;
import org.springframework.security.web.server.savedrequest.WebSessionServerRequestCache;
import org.springframework.security.web.server.util.matcher.AndServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableSpringWebSession
public class CasBeanConfig {
    @Autowired
    private CasServiceProperties casServiceProperties;

    @Bean
    public ServerRequestCache serverRequestCache() {
        WebSessionServerRequestCache requestCache = new WebSessionServerRequestCache();
        requestCache.setSaveRequestMatcher(new AndServerWebExchangeMatcher(
            Arrays.asList(new NegatedServerWebExchangeMatcher(new AndServerWebExchangeMatcher(
                new PathPatternParserServerWebExchangeMatcher("/**/favicon.ico"))))));
        return requestCache;
    }

    @Bean
    public TicketValidator ticketValidator() {
        Saml11TicketValidator tv = new Saml11TicketValidator(casServiceProperties.getServer());
        tv.setTolerance(10000L);
        return (TicketValidator) tv;
    }

    @Bean
    @Primary
    public ServerAuthenticationEntryPoint serverAuthenticationEntryPoint(CasServiceProperties casServiceProperties) {
        return new CASAuthEntryPoint(casServiceProperties);
    }

    @Bean
    public CasReactiveAuthenticationManager casAuthenticationManager(ServiceProperties sp,
                                                                     TicketValidator ticketValidator) {
        CasReactiveAuthenticationManager manager = new CasReactiveAuthenticationManager();
        manager.setServiceProperties(sp);
        manager.setTicketValidator(ticketValidator);
        manager.setAuthenticationUserDetailsService(new CasSamlUserDetailsService());
        manager.setKey("CAS_PROVIDER_LOCALHOST_9000");
        return manager;
    }

    @Bean
    public CasServerAuthenticationConverter casAuthenticationConverter(CasServiceProperties casServiceProperties) {
        return new CASAuthConverter(casServiceProperties);
    }

    @Bean
    public ServerAuthenticationSuccessHandler casAuthenticationSuccessHandler(ServiceProperties serviceProperties,
                                                                              ServerRedirectStrategy casRedirectStrategy, ServerRequestCache serverRequestCache) {
        CasServerAuthenticationSuccessHandler casAuthenticationSuccessHandler = new CasServerAuthenticationSuccessHandler();
        casAuthenticationSuccessHandler.setAuthenticateAllArtifacts(serviceProperties.isAuthenticateAllArtifacts());

        RedirectServerAuthenticationSuccessHandler redirectServerAuthenticationSuccessHandler = new RedirectServerAuthenticationSuccessHandler();
        redirectServerAuthenticationSuccessHandler.setRedirectStrategy(casRedirectStrategy);
        casAuthenticationSuccessHandler.setSuccessHandler(redirectServerAuthenticationSuccessHandler);
        return casAuthenticationSuccessHandler;
    }

    @Bean
    public ServerRedirectStrategy casRedirectStrategy(CasServiceProperties casServiceProperties) {
        CasRedirectStrategy casRedirectStrategy = new CasRedirectStrategy();
        casRedirectStrategy.setIndexUrl(casServiceProperties.getBaseUrl());
        return casRedirectStrategy;
    }

    // @Bean
    // public ServerSecurityContextRepository serverSecurityContextRepository() {
    // return new WebSessionServerSecurityContextRepository();
    // }

    @Bean
    public ReactiveSessionRepository<?> sessionRepository() {
        return new ReactiveMapSessionRepository(new ConcurrentHashMap<>());
    }

    @Primary
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        CookieWebSessionIdResolver resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("X-SESSION-ID");
        resolver.addCookieInitializer((builder) -> builder.path("/"));
        resolver.addCookieInitializer((builder) -> builder.sameSite("Strict"));

        // HeaderWebSessionIdResolver resolver = new HeaderWebSessionIdResolver();
        // resolver.setHeaderName("JSESSIONID");
        return resolver;
    }

    @Bean
    public ServerAuthenticationFailureHandler casAuthenticationFailureHandler(
        ServerAuthenticationEntryPoint serverAuthenticationEntryPoint) {
        return new ServerAuthenticationEntryPointFailureHandler(serverAuthenticationEntryPoint);
    }

    @Bean
    public AuthenticationWebFilter casAuthenticationWebFilter(CasReactiveAuthenticationManager casAuthenticationManager,
                                                              ServerSecurityContextRepository serverSecurityContextRepository,
                                                              CasServerAuthenticationConverter casAuthenticationConverter,
                                                              ServerAuthenticationSuccessHandler serverAuthenticationSuccessHandler,
                                                              ServerAuthenticationFailureHandler serverAuthenticationFailureHandler) {
        AuthenticationWebFilter casAuthenticationWebFilter = new AuthenticationWebFilter(casAuthenticationManager);
        casAuthenticationWebFilter.setRequiresAuthenticationMatcher(
            ServerWebExchangeMatchers.pathMatchers(casServiceProperties.getLoginPath()));
        casAuthenticationWebFilter.setSecurityContextRepository(serverSecurityContextRepository);
        casAuthenticationWebFilter.setServerAuthenticationConverter(casAuthenticationConverter);
        casAuthenticationWebFilter.setAuthenticationSuccessHandler(serverAuthenticationSuccessHandler);
        casAuthenticationWebFilter.setAuthenticationFailureHandler(serverAuthenticationFailureHandler);
        return casAuthenticationWebFilter;
    }

    @Bean
    public CasAuthorizationManager casAuthorizationManager(
        @Nullable CasAuthorizationDecisionHandler casAuthorizationHandler) {
        CasAuthorizationManager casAuthorizationManager = new CasAuthorizationManager();
        if (casAuthorizationHandler != null) {
            casAuthorizationManager.setCasAuthorizationHandler(casAuthorizationHandler);
        }
        return casAuthorizationManager;
    }

    @Bean
    public AuthorizationWebFilter casAuthorizationWebFilter(CasAuthorizationManager casAuthorizationManager) {
        return new AuthorizationWebFilter(casAuthorizationManager);
    }

    @Bean
    public SecurityContextServerLogoutHandler securityContextServerLogoutHandler(
        CasServiceProperties casServiceProperties) {
        return new SecurityContextServerLogoutHandler() {
            @Override
            public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
                super.logout(exchange, authentication);
                CASAuthUtil.removeAuthenticatedCookie(exchange.getExchange().getResponse());
                exchange.getExchange().getSession().map(t -> {
                    t.setMaxIdleTime(Duration.ZERO);
                    return Mono.<Void>empty();
                });
                return Mono.<Void>empty();
            }
        };
    }

    @Bean
    public RedirectServerLogoutSuccessHandler securityContextServerLogoutSuccessHandler(
        CasServiceProperties casServiceProperties) {
        return new RedirectServerLogoutSuccessHandler() {
            @Override
            public Mono<Void> onLogoutSuccess(WebFilterExchange exchange, Authentication authentication) {
                String loginUrl = casServiceProperties.getPlatformServer() + "/login";
                String redirectUrl = casServiceProperties.getServer() + "/logout?service=" + loginUrl;

                exchange.getExchange().getResponse().setStatusCode(HttpStatus.OK);
                exchange.getExchange().getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
                exchange.getExchange().getSession().map(t -> {
                    return t.invalidate();
                });
                try {
                    JSONObject map = new JSONObject();
                    map.put("status", 1);
                    map.put("mode", casServiceProperties.getMode());
                    map.put("message", "退出系统。");
                    map.put("redirectUrl", redirectUrl);
                    String stringPretty = map.toStringPretty();
                    DataBuffer buffer = exchange.getExchange().getResponse().bufferFactory()
                        .wrap(stringPretty.getBytes(Charset.defaultCharset()));
                    return exchange.getExchange().getResponse().writeWith(Mono.just(buffer))
                        .doOnError(error -> DataBufferUtils.release(buffer));
                } catch (Exception e) {
                }
                return Mono.<Void>empty();
            }
        };
    }

    @Bean
    public LogoutWebFilter logoutWebFilter(SecurityContextServerLogoutHandler securityContextServerLogoutHandler,
                                           RedirectServerLogoutSuccessHandler securityContextServerLogoutSuccessHandler) {
        LogoutWebFilter logoutWebFilter = new LogoutWebFilter();
        logoutWebFilter
            .setRequiresLogoutMatcher(ServerWebExchangeMatchers.pathMatchers(casServiceProperties.getLogoutPath()));
        logoutWebFilter.setLogoutHandler(securityContextServerLogoutHandler);
        logoutWebFilter.setLogoutSuccessHandler(securityContextServerLogoutSuccessHandler);
        return logoutWebFilter;
    }

}
