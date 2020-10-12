package com.info.baymax.dsp.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import com.info.baymax.common.saas.SaasContext;

/**
 * 处理当前租户和登录用户信息到上下文中
 *
 * @author jingwei.yang
 * @date 2019年5月15日 下午2:40:24
 */
@lombok.extern.slf4j.Slf4j
@Component
public class SaasContextWebFilter implements WebFilter, Ordered {
    @Autowired
    private SaasContextHandler saasContextHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return exchange.getPrincipal()//
                .cast(Authentication.class)//
                .flatMap(authentication -> authorizedClient(exchange, authentication))//
                .map(context -> withAuthHeaders(exchange, context))//
                .defaultIfEmpty(exchange).flatMap(chain::filter);
    }

    private Mono<SaasContext> authorizedClient(ServerWebExchange exchange, Authentication authentication) {
        if (authentication instanceof CasAuthenticationToken) {
            saasContextHandler.handle(exchange, (CasAuthenticationToken) authentication);
        }
        return Mono.just(SaasContext.getCurrentSaasContext());
    }

    private ServerWebExchange withAuthHeaders(ServerWebExchange exchange, SaasContext saasContext) {
        return exchange.mutate().request(r -> r.headers(headers -> addAuthHeaders(saasContext, headers))).build();
    }

    public void addAuthHeaders(SaasContext saasContext, HttpHeaders headers) {
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
