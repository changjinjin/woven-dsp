package com.info.baymax.dsp.gateway.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.saas.SaasContext;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * 用户认证信息绑定过滤器，当gateway拿到用户认证信息并解析后，将这些信息绑定到request中，以便于后续的业务使用，如：woven-app服务、woven-server服务获取当前租户ID、用户ID等
 *
 * @author jingwei.yang
 * @date 2019年11月11日 上午10:31:54
 */
@Slf4j
@Component
public class UserInfoRelayGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(withAuthHeaders(exchange, SaasContext.getCurrentSaasContext()));
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
                URLEncoder.encode(JSON.toJSONString(SaasContext.getCurrentSaasContext()), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public int getOrder() {
        return -200;
    }
}