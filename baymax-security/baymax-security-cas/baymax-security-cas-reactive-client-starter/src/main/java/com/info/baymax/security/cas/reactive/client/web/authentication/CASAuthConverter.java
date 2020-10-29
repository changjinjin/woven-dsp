package com.info.baymax.security.cas.reactive.client.web.authentication;

import com.info.baymax.security.cas.reactive.client.config.CasServiceProperties;
import com.info.baymax.security.cas.reactive.web.CasServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

public class CASAuthConverter extends CasServerAuthenticationConverter {

    private final CasServiceProperties casServiceProperties;

    public CASAuthConverter(CasServiceProperties casServiceProperties) {
        this.casServiceProperties = casServiceProperties;
        this.setServiceProperties(casServiceProperties);
    }

    public boolean requiresAuthentication(ServerWebExchange exchange) {
        serviceProperties.setService(casServiceProperties.getService());
        return super.requiresAuthentication(exchange);
    }
}
