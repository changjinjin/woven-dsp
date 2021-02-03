package com.info.baymax.dsp.gateway.cas;

import com.info.baymax.common.core.saas.SaasContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 处理当前租户和登录用户信息到上下文中
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午2:40:24
 */
@Slf4j
@Component
public class CasSaasContextWebFilter implements WebFilter, Ordered {
    @Autowired
    private CasSaasContextHandler saasContextHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        if (exchange.getRequest().getPath().value().startsWith("/cas") || exchange.getRequest().getPath().value().startsWith("/api")) {
            return ReactiveSecurityContextHolder.getContext()//
                .flatMap(context -> authorizedClient(exchange, context.getAuthentication()))//
                .map(context -> withAuthHeaders(exchange, context))//
                .defaultIfEmpty(exchange).flatMap(chain::filter);
        }

        return chain.filter(exchange);
    }

    public Mono<SaasContext> authorizedClient(ServerWebExchange exchange, Authentication authentication) {
        if (authentication instanceof CasAuthenticationToken) {
            saasContextHandler.handle(exchange, (CasAuthenticationToken) authentication);
        }
        return Mono.just(SaasContext.getCurrentSaasContext());
    }

    public ServerWebExchange withAuthHeaders(ServerWebExchange exchange, SaasContext saasContext) {
        return exchange.mutate().request(r -> r.headers(headers -> addAuthHeaders(saasContext, headers))).build();
    }

    private void addAuthHeaders(SaasContext saasContext, HttpHeaders headers) {
        // 调用Baymax时候需要以下信息
        if (SaasContext.getCurrentUserId() == null) {
            headers.add("temp", "true");
            return;
        }
        headers.add("userId", SaasContext.getCurrentUserId());
        headers.add("loginId", SaasContext.getCurrentUsername());
        headers.add("tenantName", SaasContext.getCurrentTenantName());
        headers.add("tenantId", SaasContext.getCurrentTenantId());

        // 调用dsp是需要以下信息
        try {
            headers.add(SaasContext.SAAS_CONTEXT_KEY,
                java.net.URLEncoder.encode(com.alibaba.fastjson.JSON.toJSONString(SaasContext.getCurrentSaasContext()), "UTF-8"));
        } catch (java.io.UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public int getOrder() {
        return -200;
    }

}
