package com.info.baymax.dsp.common.webflux.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.info.baymax.common.saas.SaasContext;
import com.info.baymax.common.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

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
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String saasContextHeader = headers.getFirst(SaasContext.SAAS_CONTEXT_KEY);
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
        }
        return chain.filter(exchange);
    }
}
