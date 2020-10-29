package com.info.baymax.security.cas.reactive.web;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

@Slf4j
@Setter
@Getter
public class CasServerAuthenticationSuccessHandler
    implements ServerAuthenticationSuccessHandler, ApplicationEventPublisherAware {
    private ApplicationEventPublisher eventPublisher;
    private boolean authenticateAllArtifacts;
    private ServerAuthenticationSuccessHandler successHandler;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange exchange, Authentication authentication) {
        // proxyTicketRequest(true,
        // exchange.getExchange().getRequest()).map(continueFilterChain -> {
        // if (!continueFilterChain) {
        // return null;
        // }
        // });
        if (log.isDebugEnabled()) {
            log.debug("Authentication success. Updating SecurityContextHolder to contain: " + authentication);
        }
        ReactiveSecurityContextHolder.withAuthentication(authentication);
        // Fire event
        if (eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authentication, this.getClass()));
        }
        if (successHandler != null) {
            return successHandler.onAuthenticationSuccess(exchange, authentication);
        }
        return Mono.empty();
    }

    private Mono<Boolean> authenticated() {
        return ReactiveSecurityContextHolder.getContext().map(t -> {
            Authentication authentication = t.getAuthentication();
            return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
        });
    }

    @SuppressWarnings("unused")
    private Mono<Boolean> proxyTicketRequest(boolean serviceTicketRequest, ServerHttpRequest request) {
        if (serviceTicketRequest) {
            return Mono.just(false);
        }
        return authenticated().map(authenticated -> {
            final boolean result = authenticateAllArtifacts && !authenticated;
            if (log.isDebugEnabled()) {
                log.debug("proxyTicketRequest = " + result);
            }
            return result;
        });
    }
}
