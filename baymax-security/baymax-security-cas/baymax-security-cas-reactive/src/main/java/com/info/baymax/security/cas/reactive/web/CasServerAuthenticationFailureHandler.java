package com.info.baymax.security.cas.reactive.web;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

public class CasServerAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    private final ServerAuthenticationFailureHandler serviceTicketFailureHandler;

    public CasServerAuthenticationFailureHandler(ServerAuthenticationFailureHandler failureHandler) {
        Assert.notNull(failureHandler, "failureHandler");
        this.serviceTicketFailureHandler = failureHandler;
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange exchange, AuthenticationException exception) {
        return serviceTicketFailureHandler.onAuthenticationFailure(exchange, exception);
    }
}