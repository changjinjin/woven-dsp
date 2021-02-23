package com.info.baymax.common.webflux.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.core.saas.SaasContext;
import com.info.baymax.common.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * SaasContext 拦截初始化
 *
 * @author jingwei.yang
 * @date 2019年12月17日 上午10:37:51
 */
@Slf4j
@Component
public class ServerSaasContextWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            HttpHeaders headers = exchange.getRequest().getHeaders();
            String saasContextHeader = headers.getFirst(SaasContext.SAAS_CONTEXT_KEY);
            String userId = headers.getFirst("userId");
            if (StringUtils.isNotEmpty(saasContextHeader)) {
                String decode = null;
                try {
                    decode = URLDecoder.decode(saasContextHeader, "UTF-8");
                    SaasContext saasContext = JsonUtils.fromJson(decode, new TypeReference<SaasContext>() {
                    });
                    if (saasContext != null) {
                        SaasContext.setCurrentSaasContext(saasContext);
                    }
                    log.debug("fix current SaasContext :{}", decode);
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                }
            } else if (StringUtils.isNotEmpty(userId)) {
                String admin = headers.getFirst("admin");
                SaasContext.initSaasContext(//
                    headers.getFirst("tenantId"), //
                    headers.getFirst("tenantName"), //
                    userId, //
                    headers.getFirst("username"), //
                    StringUtils.isNotEmpty(admin) && "true".equalsIgnoreCase(admin) ? true : false, //
                    headers.getFirst("userType") //
                );
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            SaasContext.clear();
        }
        return chain.filter(exchange);
    }
}
