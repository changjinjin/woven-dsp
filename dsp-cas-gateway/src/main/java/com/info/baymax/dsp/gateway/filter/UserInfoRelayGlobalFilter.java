package com.info.baymax.dsp.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.info.baymax.common.saas.SaasContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
        return exchange.getPrincipal().cast(Authentication.class)
            .flatMap(authentication -> authorizedClient(exchange, authentication))//
            .map(context -> withAuthHeaders(exchange, context))//
            .defaultIfEmpty(exchange).flatMap(chain::filter);
    }

    private Mono<SaasContext> authorizedClient(ServerWebExchange exchange, Authentication authentication) {
        String userInfo = null;
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
            Jwt token = authenticationToken.getToken();
            if (token != null) {
                userInfo = token.getClaimAsString("userinfo");
            }
        } else if (authentication instanceof BearerTokenAuthentication) {
            BearerTokenAuthentication authenticationToken = (BearerTokenAuthentication) authentication;
            Object principal = authenticationToken.getPrincipal();
            if (principal != null && principal instanceof OAuth2AuthenticatedPrincipal) {
                OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal = (DefaultOAuth2AuthenticatedPrincipal) principal;
                Object userAuthentication = oAuth2AuthenticatedPrincipal.getAttribute("userAuthentication");
                if (userAuthentication != null) {
                    userInfo = userAuthentication.toString();
                }
            }
        }
        if (StringUtils.isNotEmpty(userInfo)) {
            SaasContext.setCurrentSaasContext(JSON.parseObject(userInfo, SaasContext.class));
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