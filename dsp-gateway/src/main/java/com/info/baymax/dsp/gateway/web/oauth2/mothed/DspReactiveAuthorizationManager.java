package com.info.baymax.dsp.gateway.web.oauth2.mothed;

import com.info.baymax.common.utils.ICollections;
import com.info.baymax.dsp.gateway.web.mothed.RequestUriMappingsHolder;
import com.info.baymax.dsp.gateway.web.mothed.RestOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DspReactiveAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

	@Autowired
	private RequestUriMappingsHolder holder;

	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
		// @formatter:off
		return authentication
				.filter(a -> a.isAuthenticated())
				.map(a->decision(a.getAuthorities(), context.getExchange()))
				.map(hasAuthority -> new AuthorizationDecision(hasAuthority))
				.defaultIfEmpty(new AuthorizationDecision(false));
        // @formatter:on
	}

	private boolean decision(Collection<? extends GrantedAuthority> authorities, ServerWebExchange exchange) {
		RestOperation operation = holder.getOperation(exchange);
		if (operation == null || !operation.getEnabled()) {
			return true;
		}
		if (ICollections.hasNoElements(authorities)) {
			return false;
		}
		Set<String> set = authorities.stream().map(t -> t.getAuthority()).collect(Collectors.toSet());
		if (set.contains(RequestUriMappingsHolder.operationKey(operation.getFullPath(), operation.getMothed()))) {
			return true;
		}
		return false;
	}
}
