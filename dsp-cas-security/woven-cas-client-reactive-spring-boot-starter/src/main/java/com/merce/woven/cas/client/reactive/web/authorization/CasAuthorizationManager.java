package com.merce.woven.cas.client.reactive.web.authorization;

import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class CasAuthorizationManager implements ReactiveAuthorizationManager<ServerWebExchange> {

	private CasAuthorizationDecisionHandler casAuthorizationHandler = new NoOpCasAuthorizationDecisionHandler();
	private AuthenticationTrustResolver authTrustResolver = new AuthenticationTrustResolverImpl();

	private boolean isNotAnonymous(Authentication authentication) {
		return !authTrustResolver.isAnonymous(authentication);
	}

	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, ServerWebExchange exchange) {
		return authentication//
				.filter(this::isNotAnonymous)//
				.filter(auth -> auth instanceof CasAuthenticationToken)//
				.cast(CasAuthenticationToken.class)//
				.filter(auth -> auth.isAuthenticated())//
				.map(auth -> decision(auth, exchange))//
				.map(hasAuthority -> new AuthorizationDecision(hasAuthority))//
				.defaultIfEmpty(new AuthorizationDecision(true));
	}

	private boolean decision(Authentication auth, ServerWebExchange exchange) {
		return casAuthorizationHandler.handle(auth, exchange);
	}

	public void setCasAuthorizationHandler(CasAuthorizationDecisionHandler casAuthorizationHandler) {
		Assert.notNull(casAuthorizationHandler, "Parameter casAuthorizationHandler can not be null! ");
		this.casAuthorizationHandler = casAuthorizationHandler;
	}
}
