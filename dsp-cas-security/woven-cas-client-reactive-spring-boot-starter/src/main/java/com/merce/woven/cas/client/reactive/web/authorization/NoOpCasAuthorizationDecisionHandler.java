package com.merce.woven.cas.client.reactive.web.authorization;

import org.springframework.security.core.Authentication;
import org.springframework.web.server.ServerWebExchange;

public class NoOpCasAuthorizationDecisionHandler implements CasAuthorizationDecisionHandler {

	@Override
	public boolean handle(Authentication authentication, ServerWebExchange exchange) {
		return true;
	}
}
