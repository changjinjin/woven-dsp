package com.merce.woven.cas.client.reactive.web.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;

public interface CasAuthorizationDecisionHandler {
	boolean handle(Authentication authentication, ServerWebExchange exchange);
}
