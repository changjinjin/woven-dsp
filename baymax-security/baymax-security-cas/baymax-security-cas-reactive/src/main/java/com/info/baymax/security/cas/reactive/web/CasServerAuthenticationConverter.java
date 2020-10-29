package com.info.baymax.security.cas.reactive.web;

import com.info.baymax.security.cas.reactive.utils.CommonUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.proxy.ProxyGrantingTicketStorage;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Setter
@Getter
public class CasServerAuthenticationConverter implements ServerAuthenticationConverter, InitializingBean {
    public static final String CAS_STATEFUL_IDENTIFIER = "_cas_stateful_";
    public static final String CAS_STATELESS_IDENTIFIER = "_cas_stateless_";

    private ServerWebExchangeMatcher proxyReceptorMatcher;
    private ProxyGrantingTicketStorage proxyGrantingTicketStorage;
    public String artifactParameter = ServiceProperties.DEFAULT_CAS_ARTIFACT_PARAMETER;
    protected ServiceProperties serviceProperties;

    public void afterPropertiesSet() {
        Assert.hasLength(this.artifactParameter, "artifactParameter must be specified");
        Assert.notNull(this.serviceProperties, "serviceProperties must be specified");
        Assert.notNull(this.serviceProperties.getService(), "serviceProperties.getService() cannot be null.");
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        if (!requiresAuthentication(exchange)) {
            return Mono.empty();
        }

        return ReactiveSecurityContextHolder.getContext().map(t -> {
            Authentication authentication = t.getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                return authentication;
            } else {
                return extracted(exchange);
            }
        }).defaultIfEmpty(extracted(exchange));
    }

    private Authentication extracted(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        final String username = CAS_STATEFUL_IDENTIFIER;
        String password = CommonUtils.getParameter(request, artifactParameter);
        if (password == null) {
            log.debug("Failed to obtain an artifact (cas ticket)");
            password = "";
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,
            password);
        // return exchange.getSession().map(session -> {
        // if (serviceProperties != null) {
        // ServerServiceAuthenticationDetailsSource authenticationDetailsSource = new
        // ServerServiceAuthenticationDetailsSource(
        // serviceProperties);
        // authenticationToken.setDetails(authenticationDetailsSource
        // .buildDetails(request.getRemoteAddress().getHostString(), session.getId(), request));
        // }
        return authenticationToken;
        // });
    }

    public void setProxyReceptorUrl(String proxyReceptorUrl) {
        this.proxyReceptorMatcher = new PathPatternParserServerWebExchangeMatcher("/**" + proxyReceptorUrl);
    }

    public void setServiceProperties(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
        if (serviceProperties != null) {
            this.artifactParameter = serviceProperties.getArtifactParameter();
        }
    }

    @SuppressWarnings("unused")
    private Mono<Boolean> proxyReceptorRequest(ServerWebExchange exchange) {
        if (proxyReceptorMatcher == null) {
            return Mono.just(false);
        }
        return proxyReceptorMatcher.matches(exchange).map(t -> t.isMatch() && proxyReceptorConfigured());
    }

    private boolean proxyReceptorConfigured() {
        final boolean result = this.proxyGrantingTicketStorage != null && proxyReceptorMatcher != null;
        if (log.isDebugEnabled()) {
            log.debug("proxyReceptorConfigured = " + result);
        }
        return result;
    }

    public boolean requiresAuthentication(ServerWebExchange exchange) {
        return true;
    }

}
